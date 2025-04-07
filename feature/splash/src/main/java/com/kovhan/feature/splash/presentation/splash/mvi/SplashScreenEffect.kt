package com.kovhan.feature.splash.presentation.splash.mvi;

import com.kovhan.core.ui.UiEffect


sealed class SplashScreenEffect : UiEffect {
    data object NavigateToOnboarding: SplashScreenEffect()
    data object NavigateToMain: SplashScreenEffect()
}