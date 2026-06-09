package com.kovhan.feature.auth.presentation.register.mvi

import com.kovhan.core.ui.UiEffect

sealed class RegisterScreenEffect : UiEffect {
    data object LaunchGoogleSignIn : RegisterScreenEffect()
    data object NavigateToMain : RegisterScreenEffect()
    data object NavigateToSignIn : RegisterScreenEffect()
    data object NavigateBack : RegisterScreenEffect()
    data class OpenPrivacyPolicy(val url: String) : RegisterScreenEffect()
    data class OpenTermsOfService(val url: String) : RegisterScreenEffect()
}
