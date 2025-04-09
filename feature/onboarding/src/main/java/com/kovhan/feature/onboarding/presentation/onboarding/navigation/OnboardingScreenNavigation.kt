package com.kovhan.feature.onboarding.presentation.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kovhan.core.ui.navigation.OnboardingGraph
import com.kovhan.feature.onboarding.presentation.onboarding.OnboardingScreen

fun NavGraphBuilder.onboardingScreen(
    navAction: OnboardingScreenNavAction,
    paddingValues: androidx.compose.foundation.layout.PaddingValues
) {
    composable<OnboardingGraph.OnboardingScreen> {
        OnboardingScreen(
            navAction = navAction,
            paddingValues = paddingValues
        )
    }
} 