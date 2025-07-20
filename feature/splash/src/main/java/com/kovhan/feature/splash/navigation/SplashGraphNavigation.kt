package com.kovhan.feature.splash.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.navigation
import com.kovhan.core.ui.navigation.SplashGraph
import com.kovhan.core.ui.navigation.startDestination
import com.kovhan.feature.splash.presentation.splash.navigation.splashScreen

fun NavController.navigateToSplashGraph(builder: NavOptionsBuilder.() -> Unit = { }){
    navigate(
        route = SplashGraph,
        builder = builder,
    )
}

fun NavGraphBuilder.splashGraph(
    navController: NavController,
    paddingValues: PaddingValues,
    navigateToOnboarding: ()-> Unit,
    navigateToMain: ()-> Unit,
    navigateToAuth: ()-> Unit
) {
    navigation<SplashGraph>(
        startDestination = SplashGraph.startDestination
    ) {
        splashScreen(
            navAction = SplashScreenNavAction(
                onBack = {
                    navController.navigateUp()
                },
                navigateToOnboarding = navigateToOnboarding,
                navigateToMain = navigateToMain,
                navigateToAuth = navigateToAuth
            ),
            paddingValues = paddingValues
        )
    }
}