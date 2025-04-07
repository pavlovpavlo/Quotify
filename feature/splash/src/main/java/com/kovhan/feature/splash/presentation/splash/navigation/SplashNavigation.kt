package com.kovhan.feature.splash.presentation.splash.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kovhan.feature.splash.navigation.SplashScreenNavAction
import com.kovhan.feature.splash.presentation.splash.SplashScreen
import com.kovhan.feature.splash.presentation.splash.SplashScreenViewModel
import com.kovhan.feature.splash.presentation.splash.mvi.SplashScreenEffect
import kotlinx.serialization.Serializable

@Serializable
data object SplashScreen

internal fun NavGraphBuilder.splashScreen(
    navAction: SplashScreenNavAction,
    paddingValues: PaddingValues
) {
    composable<SplashScreen> {
        val viewModel = hiltViewModel<SplashScreenViewModel>()
        val state = viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.uiEffect.collect { event ->
                when (event) {
                    SplashScreenEffect.NavigateToOnboarding->{
                        navAction.navigateToOnboarding
                    }
                    SplashScreenEffect.NavigateToMain->{
                        navAction.navigateToMain
                    }
                    else->{

                    }
                }
            }
        }

        SplashScreen(
            state = state.value,
            intent = viewModel,
            paddingValues = paddingValues,
            navAction = navAction
        )
    }
}
