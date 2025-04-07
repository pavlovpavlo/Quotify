package com.kovhan.feature.splash.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.kovhan.feature.splash.presentation.splash.navigation.SplashScreen
import com.kovhan.feature.splash.presentation.splash.navigation.splashScreen
import kotlinx.serialization.Serializable


@Serializable
data object Splash

fun NavGraphBuilder.splashGraph(
    navController: NavController,
    paddingValues: PaddingValues,
    navigateToOnboarding: ()-> Unit,
    navigateToMain: ()-> Unit
) {
    navigation<Splash>(
        startDestination = SplashScreen
    ) {
        splashScreen(
            navAction = SplashScreenNavAction(
                onBack = {
                    navController.navigateUp()
                },
                navigateToOnboarding = navigateToOnboarding,
                navigateToMain = navigateToMain
            ),
            paddingValues = paddingValues
        )
    }
}