package com.kovhan.feature.auth.presentation.login

import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.ui.constants.AppLinks
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.auth.model.AuthError
import com.kovhan.domain.auth.model.AuthResult
import com.kovhan.domain.auth.use_case.SignInUseCase
import com.kovhan.domain.auth.use_case.SignInWithGoogleUseCase
import com.kovhan.domain.auth.use_case.ValidateAuthInputUseCase
import com.kovhan.core.models.onFailure
import com.kovhan.core.models.onSuccess
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
    private val signIn: SignInUseCase,
    private val signInWithGoogle: SignInWithGoogleUseCase,
    private val validateInput: ValidateAuthInputUseCase,
) : BaseViewModel<LoginScreenState, LoginScreenEffect>(LoginScreenState()),
    LoginScreenIntent {

    override fun onEmailChanged(value: TextFieldValue) = publishState { copy(email = value) }
    override fun onPasswordChanged(value: TextFieldValue) = publishState { copy(password = value) }

    override fun onSignInClicked() {
        val current = uiState.value
        if (!current.canSubmit) return

        validateInput(email = current.email.text, password = current.password.text)?.let { error ->
            showSnackbar(error.toSnackbar())
            return
        }

        publishState { copy(isLoading = true) }
        viewModelScope.launch {
            val result = signIn(current.email.text, current.password.text)
            handleResult(result)
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
            .onSuccess { publishEffect(LoginScreenEffect.NavigateToMain) }
            .onFailure { showSnackbar(it.toSnackbar()) }
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
