package com.kovhan.feature.auth.presentation.register.mvi

import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.domain.auth.AuthError

interface RegisterScreenIntent {
    fun onFullNameChanged(value: TextFieldValue)
    fun onUsernameChanged(value: TextFieldValue)
    fun onEmailChanged(value: TextFieldValue)
    fun onPasswordChanged(value: TextFieldValue)
    fun onTermsToggled(accepted: Boolean)
    fun onSignUpClicked()
    fun onGoogleSignInStarted()
    fun onGoogleIdTokenReceived(idToken: String)
    fun onGoogleSignInFailed(error: AuthError)
    fun onSignInClicked()
    fun onBackClicked()
    fun onPrivacyPolicyClicked()
    fun onTermsOfServiceClicked()

    companion object {
        val Empty: RegisterScreenIntent = object : RegisterScreenIntent {
            override fun onFullNameChanged(value: TextFieldValue) = Unit
            override fun onUsernameChanged(value: TextFieldValue) = Unit
            override fun onEmailChanged(value: TextFieldValue) = Unit
            override fun onPasswordChanged(value: TextFieldValue) = Unit
            override fun onTermsToggled(accepted: Boolean) = Unit
            override fun onSignUpClicked() = Unit
            override fun onGoogleSignInStarted() = Unit
            override fun onGoogleIdTokenReceived(idToken: String) = Unit
            override fun onGoogleSignInFailed(error: AuthError) = Unit
            override fun onSignInClicked() = Unit
            override fun onBackClicked() = Unit
            override fun onPrivacyPolicyClicked() = Unit
            override fun onTermsOfServiceClicked() = Unit
        }
    }
}
