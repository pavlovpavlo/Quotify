package com.kovhan.feature.splash.presentation.splash.mvi

import com.kovhan.core.ui.UiEffect

sealed class SplashScreenEffect : UiEffect {
    data object NavigateToOnboarding : SplashScreenEffect()

    /**
     * Once onboarded but before the auth state lives in storage, "I'm done with
     * splash" always means "go to the auth flow". Replace with NavigateToMain
     * once a user session can be read on app start.
     */
    data object NavigateToAuth : SplashScreenEffect()
}
