package com.kovhan.feature.auth.presentation.register.mvi

import com.kovhan.core.ui.UiEffect

sealed class RegisterScreenEffect : UiEffect {
    data object None : RegisterScreenEffect()
} 