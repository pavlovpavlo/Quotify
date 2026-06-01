package com.kovhan.feature.auth.presentation.register

import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.ui.constants.AppLinks
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.auth.AuthError
import com.kovhan.domain.auth.AuthResult
import com.kovhan.domain.auth.SignInWithGoogleUseCase
import com.kovhan.domain.auth.SignUpUseCase
import com.kovhan.domain.auth.ValidateAuthInputUseCase
import com.kovhan.domain.auth.onFailure
import com.kovhan.domain.auth.onSuccess
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

    override fun onGoogleSignInStarted() {
        publishState { copy(isGoogleLoading = true) }
    }

    override fun onGoogleIdTokenReceived(idToken: String) {
        viewModelScope.launch {
            handleResult(signInWithGoogle(idToken))
        }
    }

    override fun onGoogleSignInFailed(error: AuthError) {
        publishState { copy(isGoogleLoading = false) }
        if (error != AuthError.GoogleSignInCancelled) {
            showSnackbar(error.toSnackbar())
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
