package com.kovhan.feature.auth.presentation.forgot_password.mvi

import androidx.compose.ui.text.input.TextFieldValue

interface ForgotPasswordScreenIntent {
    fun onEmailChanged(value: TextFieldValue)
    fun onSendClicked()
    fun onBackToSignInClicked()
    fun onBackClicked()

    companion object {
        val Empty: ForgotPasswordScreenIntent = object : ForgotPasswordScreenIntent {
            override fun onEmailChanged(value: TextFieldValue) = Unit
            override fun onSendClicked() = Unit
            override fun onBackToSignInClicked() = Unit
            override fun onBackClicked() = Unit
        }
    }
}
