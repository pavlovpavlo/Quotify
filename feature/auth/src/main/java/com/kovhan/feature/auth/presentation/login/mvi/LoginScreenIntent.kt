package com.kovhan.feature.auth.presentation.login.mvi

import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.domain.auth.AuthError

interface LoginScreenIntent {
    fun onEmailChanged(value: TextFieldValue)
    fun onPasswordChanged(value: TextFieldValue)
    fun onSignInClicked()
    fun onGoogleSignInStarted()
    fun onGoogleIdTokenReceived(idToken: String)
    fun onGoogleSignInFailed(error: AuthError)
    fun onForgotPasswordClicked()
    fun onSignUpClicked()
    fun onBackClicked()
    fun onPrivacyPolicyClicked()
    fun onTermsOfServiceClicked()

    companion object {
        val Empty: LoginScreenIntent = object : LoginScreenIntent {
            override fun onEmailChanged(value: TextFieldValue) = Unit
            override fun onPasswordChanged(value: TextFieldValue) = Unit
            override fun onSignInClicked() = Unit
            override fun onGoogleSignInStarted() = Unit
            override fun onGoogleIdTokenReceived(idToken: String) = Unit
            override fun onGoogleSignInFailed(error: AuthError) = Unit
            override fun onForgotPasswordClicked() = Unit
            override fun onSignUpClicked() = Unit
            override fun onBackClicked() = Unit
            override fun onPrivacyPolicyClicked() = Unit
            override fun onTermsOfServiceClicked() = Unit
        }
    }
}
