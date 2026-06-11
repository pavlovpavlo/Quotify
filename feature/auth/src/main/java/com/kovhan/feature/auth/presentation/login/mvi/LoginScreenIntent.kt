package com.kovhan.feature.auth.presentation.login.mvi

import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.domain.auth.GoogleSignInOutcome

interface LoginScreenIntent {
    fun onEmailChanged(value: TextFieldValue)
    fun onPasswordChanged(value: TextFieldValue)
    fun onSignInClicked()
    fun onGoogleSignInClicked()
    fun onGoogleSignInResult(outcome: GoogleSignInOutcome)
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
            override fun onGoogleSignInClicked() = Unit
            override fun onGoogleSignInResult(outcome: GoogleSignInOutcome) = Unit
            override fun onForgotPasswordClicked() = Unit
            override fun onSignUpClicked() = Unit
            override fun onBackClicked() = Unit
            override fun onPrivacyPolicyClicked() = Unit
            override fun onTermsOfServiceClicked() = Unit
        }
    }
}
