package com.kovhan.feature.onboarding.presentation.onboarding.navigation

data class OnboardingScreenNavAction(
    val onBack: () -> Unit,
    val navigateToMain: () -> Unit
) 