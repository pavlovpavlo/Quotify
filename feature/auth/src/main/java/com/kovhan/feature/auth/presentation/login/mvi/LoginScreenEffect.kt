package com.kovhan.feature.auth.presentation.login.mvi

import com.kovhan.core.ui.UiEffect

sealed class LoginScreenEffect : UiEffect {
    data object None : LoginScreenEffect()
} 