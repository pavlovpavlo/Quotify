package com.kovhan.feature.splash.presentation.splash.mvi

import com.kovhan.core.ui.UiEffect

sealed class SplashScreenEffect : UiEffect {
    data object NavigateToOnboarding : SplashScreenEffect()

    data object NavigateToAuth : SplashScreenEffect()

    data object NavigateToMain : SplashScreenEffect()

    /** Те саме, що [NavigateToMain], але з екраном підписки поверх бібліотеки. */
    data object NavigateToMainWithPaywall : SplashScreenEffect()

    /** Offline with no subscription — show the blocking retry dialog. */
    data object ShowOfflineBlock : SplashScreenEffect()

    /** Встановлена версія нижча за мінімальну з конфігу — показати блокуючий діалог. */
    data object ShowUpdateRequired : SplashScreenEffect()
}
