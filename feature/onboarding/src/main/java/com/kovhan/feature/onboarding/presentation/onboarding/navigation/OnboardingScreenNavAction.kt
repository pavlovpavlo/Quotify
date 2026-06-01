package com.kovhan.feature.onboarding.presentation.onboarding.navigation

import androidx.compose.runtime.Stable

@Stable
interface OnboardingScreenNavAction {
    fun onBack()
    fun navigateToComplete()

    companion object {
        val Empty: OnboardingScreenNavAction = object : OnboardingScreenNavAction {
            override fun onBack() = Unit
            override fun navigateToComplete() = Unit
        }
    }
}
