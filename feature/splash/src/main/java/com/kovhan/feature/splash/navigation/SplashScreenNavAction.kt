package com.kovhan.feature.splash.navigation

import androidx.compose.runtime.Stable

@Stable
interface SplashScreenNavAction {
    fun onBack()
    fun navigateToOnboarding()
    fun navigateToMain()
    fun navigateToAuth()

    companion object {
        val Empty: SplashScreenNavAction = EmptySplashScreenNavAction
    }
}

private object EmptySplashScreenNavAction : SplashScreenNavAction {
    override fun onBack() = Unit
    override fun navigateToOnboarding() = Unit
    override fun navigateToMain() = Unit
    override fun navigateToAuth() = Unit
}