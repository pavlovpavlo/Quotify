package com.kovhan.feature.onboarding.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.kovhan.core.ui.navigation.OnboardingGraph
import com.kovhan.core.ui.navigation.startDestination
import com.kovhan.feature.onboarding.presentation.onboarding.navigation.OnboardingScreenNavAction
import com.kovhan.feature.onboarding.presentation.onboarding.navigation.onboardingScreen

fun NavGraphBuilder.onboardingGraph(
    navController: NavController,
    paddingValues: PaddingValues,
    navigateToMain: () -> Unit
) {
    navigation<OnboardingGraph>(
        startDestination = OnboardingGraph.startDestination
    ) {
        onboardingScreen(
            navAction = OnboardingScreenNavAction(
                onBack = {
                    navController.navigateUp()
                },
                navigateToMain = navigateToMain
            ),
            paddingValues = paddingValues
        )
    }
} 