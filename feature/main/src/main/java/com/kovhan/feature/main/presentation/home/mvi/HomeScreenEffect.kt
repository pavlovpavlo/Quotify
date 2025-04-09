package com.kovhan.feature.main.presentation.home.mvi

import com.kovhan.core.ui.UiEffect

sealed class HomeScreenEffect : UiEffect {
    data object None : HomeScreenEffect()
} 