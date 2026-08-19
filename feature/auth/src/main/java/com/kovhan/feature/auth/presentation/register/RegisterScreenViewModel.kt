package com.kovhan.feature.auth.presentation.register

import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.analytics.AnalyticsTracker
import com.kovhan.core.analytics.AuthEntry
import com.kovhan.core.analytics.AuthProvider
import com.kovhan.core.analytics.event.SignUpFailed
import com.kovhan.core.analytics.event.SignUpFinished
import com.kovhan.core.analytics.event.SignUpOpened
import com.kovhan.core.ui.constants.AppLinks
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.auth.model.AuthError
import com.kovhan.domain.auth.model.AuthResult
import com.kovhan.domain.auth.use_case.ValidateAuthInputUseCase
import com.kovhan.domain.auth.use_case.guest.GuestAwareGoogleSignInUseCase
import com.kovhan.domain.auth.use_case.guest.GuestAwareSignUpUseCase
import com.kovhan.core.models.onFailure
import com.kovhan.core.models.onSuccess
import com.kovhan.feature.auth.presentation.analytics.toFailureName
import com.kovhan.feature.auth.presentation.google.GoogleSignInOutcome
import com.kovhan.feature.auth.presentation.register.mvi.RegisterScreenEffect
import com.kovhan.feature.auth.presentation.register.mvi.RegisterScreenIntent
import com.kovhan.feature.auth.presentation.register.mvi.RegisterScreenState
import com.kovhan.feature.auth.presentation.util.toSnackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    private val signUp: GuestAwareSignUpUseCase,
    private val signInWithGoogle: GuestAwareGoogleSignInUseCase,
    private val validateInput: ValidateAuthInputUseCase,
    private val analytics: AnalyticsTracker,
) : BaseViewModel<RegisterScreenState, RegisterScreenEffect>(RegisterScreenState()),
    RegisterScreenIntent {

    private var entry: AuthEntry = AuthEntry.FIRST_LAUNCH

    fun onScreenOpened(entry: AuthEntry) {
        this.entry = entry
        analytics.track(SignUpOpened(entry))
    }

    override fun onFullNameChanged(value: TextFieldValue) = publishState { copy(fullName = value) }
    override fun onUsernameChanged(value: TextFieldValue) = publishState { copy(username = value, errorMessage = null, errorValidationMessage = null) }
    override fun onEmailChanged(value: TextFieldValue) = publishState { copy(email = value, errorMessage = null, errorValidationMessage = null) }
    override fun onPasswordChanged(value: TextFieldValue) = publishState { copy(password = value, errorMessage = null, errorValidationMessage = null) }
    override fun onTermsToggled(accepted: Boolean) = publishState { copy(termsAccepted = accepted) }

    override fun onSignUpClicked() {
        val current = uiState.value
        if (!current.canSubmit) return

        validateInput(
            name = current.username.text,
            email = current.email.text,
            password = current.password.text,
        )?.let { error ->
            publishState { copy(errorValidationMessage = error) }
            return
        }

        publishState { copy(isLoading = true) }
        viewModelScope.launch {
            val displayName = current.fullName.text.ifBlank { current.username.text }
            handleResult(
                result = signUp(
                    email = current.email.text,
                    password = current.password.text,
                    username = current.username.text,
                    displayName = displayName,
                ),
                provider = AuthProvider.EMAIL,
            )
        }
    }

    override fun onGoogleSignInClicked() {
        if (uiState.value.isGoogleLoading) return
        publishState { copy(isGoogleLoading = true) }
        publishEffect(RegisterScreenEffect.LaunchGoogleSignIn)
    }

    override fun onGoogleSignInResult(outcome: GoogleSignInOutcome) {
        when (outcome) {
            is GoogleSignInOutcome.Success -> viewModelScope.launch {
                handleResult(signInWithGoogle(outcome.idToken), AuthProvider.GOOGLE)
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
                analytics.track(SignUpFinished(entry, provider))
                publishEffect(RegisterScreenEffect.NavigateToMain)
            }
            .onFailure {
                analytics.track(SignUpFailed(entry, it.toFailureName()))
                publishState { copy(errorMessage = it) }
            }
    }

    override fun onSignInClicked() {
        publishEffect(RegisterScreenEffect.NavigateToSignIn)
    }

    override fun onBackClicked() {
        publishEffect(RegisterScreenEffect.NavigateBack)
    }

    override fun onPrivacyPolicyClicked() {
        publishEffect(RegisterScreenEffect.OpenPrivacyPolicy(AppLinks.PRIVACY_POLICY))
    }

    override fun onTermsOfServiceClicked() {
        publishEffect(RegisterScreenEffect.OpenTermsOfService(AppLinks.TERMS_OF_SERVICE))
    }
}
