package com.kovhan.quotify

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kovhan.core.ui.navigation.AuthGraph
import com.kovhan.core.ui.navigation.MainGraph
import com.kovhan.core.ui.navigation.OnboardingGraph
import com.kovhan.core.ui.navigation.SplashGraph
import com.kovhan.feature.auth.navigation.authGraph
import com.kovhan.feature.auth.navigation.navigateToAuthGraph
import com.kovhan.feature.main.navigation.mainGraph
import com.kovhan.feature.main.navigation.navigateToMainGraph
import com.kovhan.feature.onboarding.navigation.navigateToOnboardingGraph
import com.kovhan.feature.onboarding.navigation.onboardingGraph
import com.kovhan.feature.splash.navigation.splashGraph
import com.kovhan.quotify.mvi.MainActivityState
import com.kovhan.quotify.mvi.MainIntent
import com.kovhan.quotify.navigation.BottomBar
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val mainScope = MainScope()

/**
 * Визначає, чи потрібно показувати BottomBar для поточного destination
 */
private fun shouldShowBottomBar(destination: NavDestination?): Boolean {
    if (destination == null) return false

    val route = destination.route ?: ""
    return route.contains("Home", ignoreCase = true) ||
           route.contains("Quotes", ignoreCase = true) ||
           route.contains("Favorites", ignoreCase = true) ||
           route.contains("Profile", ignoreCase = true) ||
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
    var showBottomBar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            snackBarSuccessHostState.currentSnackbarData?.dismiss()
            snackBarErrorHostState.currentSnackbarData?.dismiss()
            
            val shouldShowBottomBar = shouldShowBottomBar(destination)
            
            if (shouldShowBottomBar && !showBottomBar) {
                mainScope.launch {
                    delay(150)
                    showBottomBar = true
                }
            } else if (!shouldShowBottomBar && showBottomBar) {
                showBottomBar = false
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 150) // Швидша анімація появи
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 100) // Швидша анімація зникнення
                )
            ) {
                BottomBar(navController = navController)
            }
        }
    ) { innerPaddingModifier ->
        NavHost(
            modifier = modifier.fillMaxSize(),
            navController = navController,
            startDestination = SplashGraph::class.qualifiedName!!
        ) {
            splashGraph(
                navController = navController,
                paddingValues = innerPaddingModifier,
                navigateToOnboarding = {
                    navController.navigateToOnboardingGraph {
                        popUpTo(SplashGraph) { inclusive = true }
                    }
                },
                navigateToMain = {
                    navController.navigateToMainGraph {
                        popUpTo(SplashGraph) { inclusive = true }
                    }
                },
                navigateToAuth = {
                    navController.navigateToAuthGraph {
                        popUpTo(SplashGraph) { inclusive = true }
                    }
                }
            )

            onboardingGraph(
                navController = navController,
                paddingValues = innerPaddingModifier,
                navigateToMain = {
                    navController.navigateToMainGraph {
                        popUpTo(OnboardingGraph) { inclusive = true }
                    }
                }
            )

            authGraph(
                navController = navController,
                paddingValues = innerPaddingModifier
            )

            mainGraph(
                navController = navController,
                paddingValues = innerPaddingModifier
            )
        }
    }
}