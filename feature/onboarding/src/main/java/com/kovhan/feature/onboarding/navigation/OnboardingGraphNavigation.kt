package com.kovhan.feature.onboarding.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.navigation
import com.kovhan.core.ui.navigation.OnboardingGraph
import com.kovhan.core.ui.navigation.startDestination
import com.kovhan.feature.onboarding.presentation.onboarding.navigation.OnboardingScreenNavAction
import com.kovhan.feature.onboarding.presentation.onboarding.navigation.onboardingScreen

fun NavController.navigateToOnboardingGraph(builder: NavOptionsBuilder.() -> Unit = { }) {
    navigate(route = OnboardingGraph, builder = builder)
}

fun NavGraphBuilder.onboardingGraph(
    navController: NavController,
    paddingValues: PaddingValues,
    navigateToComplete: () -> Unit,
) {
    navigation<OnboardingGraph>(startDestination = OnboardingGraph.startDestination) {
        onboardingScreen(
            navAction = object : OnboardingScreenNavAction {
                override fun onBack() {
                    navController.navigateUp()
                }
                override fun navigateToComplete() {
                    navigateToComplete()
                }
            },
            paddingValues = paddingValues,
        )
    }
}
