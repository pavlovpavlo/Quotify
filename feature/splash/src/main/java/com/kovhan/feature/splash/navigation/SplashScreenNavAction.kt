package com.kovhan.feature.splash.navigation

class SplashScreenNavAction(
    val onBack: () -> Unit = { },
    val navigateToOnboarding: () -> Unit = { },
    val navigateToMain: () -> Unit = { },
)