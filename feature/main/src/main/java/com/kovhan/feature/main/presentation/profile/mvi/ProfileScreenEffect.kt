package com.kovhan.feature.main.presentation.profile.mvi

import com.kovhan.core.ui.UiEffect

sealed class ProfileScreenEffect : UiEffect {
    data object None : ProfileScreenEffect()
} 