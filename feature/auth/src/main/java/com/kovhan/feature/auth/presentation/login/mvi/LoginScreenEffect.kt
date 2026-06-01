package com.kovhan.feature.auth.presentation.login.mvi

import com.kovhan.core.ui.UiEffect

sealed class LoginScreenEffect : UiEffect {
    data object NavigateToMain : LoginScreenEffect()
    data object NavigateToSignUp : LoginScreenEffect()
    data object NavigateToForgotPassword : LoginScreenEffect()
    data object NavigateBack : LoginScreenEffect()
    data class OpenPrivacyPolicy(val url: String) : LoginScreenEffect()
    data class OpenTermsOfService(val url: String) : LoginScreenEffect()
}
