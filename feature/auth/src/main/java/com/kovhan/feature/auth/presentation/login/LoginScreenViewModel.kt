package com.kovhan.feature.auth.presentation.login

import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.analytics.AccountDeleteResult
import com.kovhan.core.analytics.AnalyticsTracker
import com.kovhan.core.analytics.AuthEntry
import com.kovhan.core.analytics.AuthProvider
import com.kovhan.core.analytics.event.AccountDeleteFinished
import com.kovhan.core.analytics.event.SignInFailed
import com.kovhan.core.analytics.event.SignInFinished
import com.kovhan.core.analytics.event.SignInOpened
import com.kovhan.core.ui.constants.AppLinks
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.auth.model.AuthError
import com.kovhan.domain.auth.model.AuthResult
import com.kovhan.domain.auth.use_case.ConfirmDeleteWithGoogleUseCase
import com.kovhan.domain.auth.use_case.ConfirmDeleteWithPasswordUseCase
import com.kovhan.domain.auth.use_case.ValidateAuthInputUseCase
import com.kovhan.domain.auth.use_case.guest.GuestAwareGoogleSignInUseCase
import com.kovhan.domain.auth.use_case.guest.GuestAwareSignInUseCase
import com.kovhan.core.models.onFailure
import com.kovhan.core.models.onSuccess
import com.kovhan.feature.auth.presentation.analytics.toFailureName
import com.kovhan.feature.auth.presentation.google.GoogleSignInOutcome
import com.kovhan.feature.auth.presentation.login.mvi.LoginScreenEffect
import com.kovhan.feature.auth.presentation.login.mvi.LoginScreenIntent
import com.kovhan.feature.auth.presentation.login.mvi.LoginScreenState
import com.kovhan.feature.auth.presentation.util.toSnackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val signIn: GuestAwareSignInUseCase,
    private val signInWithGoogle: GuestAwareGoogleSignInUseCase,
    private val validateInput: ValidateAuthInputUseCase,
    private val confirmDeleteWithPassword: ConfirmDeleteWithPasswordUseCase,
    private val confirmDeleteWithGoogle: ConfirmDeleteWithGoogleUseCase,
    private val analytics: AnalyticsTracker,
) : BaseViewModel<LoginScreenState, LoginScreenEffect>(LoginScreenState()),
    LoginScreenIntent {

    private var entry: AuthEntry = AuthEntry.FIRST_LAUNCH

    fun onScreenOpened(entry: AuthEntry) {
        this.entry = entry
        analytics.track(SignInOpened(entry))
    }

    /** Switches the screen into the re-authentication step for confirming account deletion. */
    fun enableConfirmDelete() = publishState { copy(confirmDelete = true) }

    override fun onEmailChanged(value: TextFieldValue) = publishState { copy(email = value, errorMessage = null, errorValidationMessage = null) }
    override fun onPasswordChanged(value: TextFieldValue) = publishState { copy(password = value, errorMessage = null, errorValidationMessage = null) }

    override fun onSignInClicked() {
        val current = uiState.value
        if (!current.canSubmit) return

        validateInput(email = current.email.text, password = current.password.text)?.let { error ->
            publishState { copy(errorValidationMessage = error) }
            return
        }

        publishState { copy(isLoading = true) }
        viewModelScope.launch {
            if (current.confirmDelete) {
                handleDeleteResult(confirmDeleteWithPassword(current.email.text, current.password.text))
            } else {
                handleResult(signIn(current.email.text, current.password.text), AuthProvider.EMAIL)
            }
        }
    }

    override fun onGoogleSignInClicked() {
        if (uiState.value.isGoogleLoading) return
        publishState { copy(isGoogleLoading = true) }
        publishEffect(LoginScreenEffect.LaunchGoogleSignIn)
    }

    override fun onGoogleSignInResult(outcome: GoogleSignInOutcome) {
        when (outcome) {
            is GoogleSignInOutcome.Success -> viewModelScope.launch {
                if (uiState.value.confirmDelete) {
                    handleDeleteResult(confirmDeleteWithGoogle(outcome.idToken))
                } else {
                    handleResult(signInWithGoogle(outcome.idToken), AuthProvider.GOOGLE)
                }
            }
            is GoogleSignInOutcome.Failure -> {
                publishState { copy(isGoogleLoading = false) }
                if (outcome.error != AuthError.GoogleSignInCancelled) {
                    publishState { copy(errorMessage = outcome.error) }
                }
            }
        }
    }

    private fun <T> handleResult(result: AuthResult<T>, provider: AuthProvider) {
        publishState { copy(isLoading = false, isGoogleLoading = false) }
        result
            .onSuccess {
                analytics.track(SignInFinished(entry, provider))
                publishEffect(LoginScreenEffect.NavigateToMain)
            }
            .onFailure {
                analytics.track(SignInFailed(entry, it.toFailureName()))
                publishState { copy(errorMessage = it) }
            }
    }

    private fun handleDeleteResult(result: AuthResult<Unit>) {
        publishState { copy(isLoading = false, isGoogleLoading = false) }
        result
            .onSuccess {
                analytics.track(AccountDeleteFinished(AccountDeleteResult.DELETED))
                publishEffect(LoginScreenEffect.DeleteCompleted)
            }
            .onFailure { publishState { copy(errorMessage = it) } }
    }

    override fun onForgotPasswordClicked() {
        publishEffect(LoginScreenEffect.NavigateToForgotPassword)
    }

    override fun onSignUpClicked() {
        publishEffect(LoginScreenEffect.NavigateToSignUp)
    }

    override fun onBackClicked() {
        publishEffect(LoginScreenEffect.NavigateBack)
    }

    override fun onPrivacyPolicyClicked() {
        publishEffect(LoginScreenEffect.OpenPrivacyPolicy(AppLinks.PRIVACY_POLICY))
    }

    override fun onTermsOfServiceClicked() {
        publishEffect(LoginScreenEffect.OpenTermsOfService(AppLinks.TERMS_OF_SERVICE))
    }
}
