package com.kovhan.feature.onboarding.presentation.onboarding.mvi

interface OnboardingScreenIntent {
    fun onPageChanged(page: Int)
    fun onNextClicked()
    fun onSkipClicked()

    companion object {
        val Empty: OnboardingScreenIntent = object : OnboardingScreenIntent {
            override fun onPageChanged(page: Int) = Unit
            override fun onNextClicked() = Unit
            override fun onSkipClicked() = Unit
        }
    }
}
