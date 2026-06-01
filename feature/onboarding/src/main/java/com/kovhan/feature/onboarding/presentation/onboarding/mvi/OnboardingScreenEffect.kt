package com.kovhan.feature.onboarding.presentation.onboarding.mvi

import com.kovhan.core.ui.UiEffect

sealed class OnboardingScreenEffect : UiEffect {
    data class GoToPage(val page: Int) : OnboardingScreenEffect()
    /** Onboarding finished — hand off to the post-onboarding Complete graph. */
    data object NavigateToComplete : OnboardingScreenEffect()
}
