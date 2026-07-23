package com.kovhan.feature.auth.presentation.register

import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.ui.constants.AppLinks
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.auth.model.AuthError
import com.kovhan.domain.auth.model.AuthResult
import com.kovhan.domain.auth.use_case.ValidateAuthInputUseCase
import com.kovhan.domain.auth.use_case.guest.GuestAwareGoogleSignInUseCase
import com.kovhan.domain.auth.use_case.guest.GuestAwareSignUpUseCase
import com.kovhan.core.models.onFailure
import com.kovhan.core.models.onSuccess
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
) : BaseViewModel<RegisterScreenState, RegisterScreenEffect>(RegisterScreenState()),
    RegisterScreenIntent {

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
                signUp(
                    email = current.email.text,
                    password = current.password.text,
                    username = current.username.text,
                    displayName = displayName,
                ),
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
                handleResult(signInWithGoogle(outcome.idToken))
            }
            is GoogleSignInOutcome.Failure -> {
                publishState { copy(isGoogleLoading = false) }
                if (outcome.error != AuthError.GoogleSignInCancelled) {
                    publishState { copy(errorMessage = outcome.error) }
                }
            }
        }
    }

    private fun <T> handleResult(result: AuthResult<T>) {
        publishState { copy(isLoading = false, isGoogleLoading = false) }
        result
            .onSuccess { publishEffect(RegisterScreenEffect.NavigateToMain) }
            .onFailure { publishState { copy(errorMessage = it) } }
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
