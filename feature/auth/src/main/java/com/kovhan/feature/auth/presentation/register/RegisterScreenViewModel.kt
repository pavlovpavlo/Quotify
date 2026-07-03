package com.kovhan.feature.auth.presentation.register

import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.ui.constants.AppLinks
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.auth.model.AuthError
import com.kovhan.domain.auth.model.AuthResult
import com.kovhan.domain.auth.use_case.SignInWithGoogleUseCase
import com.kovhan.domain.auth.use_case.SignUpUseCase
import com.kovhan.domain.auth.use_case.ValidateAuthInputUseCase
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
    private val signUp: SignUpUseCase,
    private val signInWithGoogle: SignInWithGoogleUseCase,
    private val validateInput: ValidateAuthInputUseCase,
) : BaseViewModel<RegisterScreenState, RegisterScreenEffect>(RegisterScreenState()),
    RegisterScreenIntent {

    override fun onFullNameChanged(value: TextFieldValue) = publishState { copy(fullName = value) }
    override fun onUsernameChanged(value: TextFieldValue) = publishState { copy(username = value) }
    override fun onEmailChanged(value: TextFieldValue) = publishState { copy(email = value) }
    override fun onPasswordChanged(value: TextFieldValue) = publishState { copy(password = value) }
    override fun onTermsToggled(accepted: Boolean) = publishState { copy(termsAccepted = accepted) }

    override fun onSignUpClicked() {
        val current = uiState.value
        if (!current.canSubmit) return

        validateInput(
            name = current.username.text,
            email = current.email.text,
            password = current.password.text,
        )?.let { error ->
            showSnackbar(error.toSnackbar())
            return
        }

        publishState { copy(isLoading = true) }
        viewModelScope.launch {
            val displayName = current.fullName.text.ifBlank { current.username.text }
            handleResult(signUp(current.email.text, current.password.text, displayName))
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
                    showSnackbar(outcome.error.toSnackbar())
                }
            }
        }
    }

    private fun <T> handleResult(result: AuthResult<T>) {
        publishState { copy(isLoading = false, isGoogleLoading = false) }
        result
            .onSuccess { publishEffect(RegisterScreenEffect.NavigateToMain) }
            .onFailure { showSnackbar(it.toSnackbar()) }
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
