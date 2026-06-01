package com.kovhan.quotify

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kovhan.core.ui.navigation.AuthGraph
import com.kovhan.core.ui.navigation.MainGraph
import com.kovhan.core.ui.navigation.SplashGraph
import com.kovhan.feature.auth.navigation.authGraph
import com.kovhan.feature.auth.navigation.completeGraph
import com.kovhan.feature.auth.navigation.navigateToAuthGraph
import com.kovhan.feature.auth.navigation.navigateToCompleteGraph
import com.kovhan.feature.main.navigation.mainGraph
import com.kovhan.feature.main.navigation.navigateToMainGraph
import com.kovhan.feature.onboarding.navigation.navigateToOnboardingGraph
import com.kovhan.feature.onboarding.navigation.onboardingGraph
import com.kovhan.feature.splash.navigation.splashGraph
import com.kovhan.feature.webview.navigation.navigateToWebView
import com.kovhan.feature.webview.navigation.webViewGraph
import com.kovhan.quotify.mvi.MainActivityState
import com.kovhan.quotify.mvi.MainIntent
import com.kovhan.quotify.navigation.dock.BottomDock

private fun isMainTabRoute(destination: NavDestination?): Boolean {
    val route = destination?.route ?: return false
    return route.contains("Quotes", ignoreCase = true) ||
        route.contains("Profile", ignoreCase = true) ||
        route.contains("Home", ignoreCase = true) ||
        route.contains("Favorites", ignoreCase = true) ||
        route.contains("MainGraph", ignoreCase = true)
}

@Composable
fun AppContent(
    modifier: Modifier = Modifier,
    uiState: MainActivityState,
    uiIntent: MainIntent,
    navController: NavHostController = rememberNavController(),
) {
    val snackBarSuccessHostState = remember { SnackbarHostState() }
    val snackBarErrorHostState = remember { SnackbarHostState() }
    var showDock by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            snackBarSuccessHostState.currentSnackbarData?.dismiss()
            snackBarErrorHostState.currentSnackbarData?.dismiss()
            showDock = isMainTabRoute(destination)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPaddingModifier ->
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                modifier = modifier.fillMaxSize(),
                navController = navController,
                startDestination = SplashGraph::class.qualifiedName!!,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
            ) {
                splashGraph(
                    navController = navController,
                    paddingValues = innerPaddingModifier,
                    navigateToOnboarding = {
                        navController.navigateToOnboardingGraph {
                            popUpTo(SplashGraph) { inclusive = true }
                        }
                    },
                    navigateToAuth = {
                        navController.navigateToAuthGraph {
                            popUpTo(SplashGraph) { inclusive = true }
                        }
                    },
                )

                onboardingGraph(
                    navController = navController,
                    paddingValues = innerPaddingModifier,
                    navigateToComplete = {
                        navController.navigateToCompleteGraph()
                    },
                )

                completeGraph(
                    navController = navController,
                    paddingValues = innerPaddingModifier,
                    navigateToSignUp = {
                        navController.navigate(AuthGraph.RegisterScreen)
                    },
                    navigateToSignIn = {
                        navController.navigateToAuthGraph()
                    },
                    navigateToMain = {
                        navController.navigateToMainGraph()
                    },
                )

                authGraph(
                    navController = navController,
                    paddingValues = innerPaddingModifier,
                    navigateToMain = {
                        navController.navigateToMainGraph {
                            popUpTo(AuthGraph) { inclusive = true }
                        }
                    },
                    navigateToWebView = { title, url ->
                        navController.navigateToWebView(title = title, url = url)
                    },
                )

                mainGraph(
                    navController = navController,
                    paddingValues = innerPaddingModifier,
                    navigateToAuth = {
                        navController.navigateToAuthGraph {
                            popUpTo(MainGraph) { inclusive = true }
                        }
                    },
                )

                webViewGraph(
                    navController = navController,
                    paddingValues = innerPaddingModifier,
                )
            }

            AnimatedVisibility(
                visible = showDock,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                BottomDock(navController = navController)
            }
        }
    }
}
