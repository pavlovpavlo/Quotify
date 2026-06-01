package com.kovhan.feature.auth.presentation.forgot_password.mvi

import com.kovhan.core.ui.UiEffect

sealed class ForgotPasswordScreenEffect : UiEffect {
    data object NavigateToSignIn : ForgotPasswordScreenEffect()
    data object NavigateBack : ForgotPasswordScreenEffect()
}
