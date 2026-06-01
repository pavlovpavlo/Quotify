package com.kovhan.feature.onboarding.presentation.onboarding.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kovhan.core.ui.navigation.OnboardingGraph
import com.kovhan.feature.onboarding.presentation.onboarding.OnboardingScreen
import com.kovhan.feature.onboarding.presentation.onboarding.OnboardingScreenViewModel
import com.kovhan.feature.onboarding.presentation.onboarding.mvi.OnboardingScreenEffect

internal fun NavGraphBuilder.onboardingScreen(
    navAction: OnboardingScreenNavAction,
    paddingValues: PaddingValues,
) {
    composable<OnboardingGraph.OnboardingScreen> {
        val viewModel = hiltViewModel<OnboardingScreenViewModel>()
        val state = viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    is OnboardingScreenEffect.GoToPage -> Unit // handled inside the screen via state
                    OnboardingScreenEffect.NavigateToComplete -> navAction.navigateToComplete()
                }
            }
        }

        OnboardingScreen(
            state = state.value,
            intent = viewModel,
            navAction = navAction,
            paddingValues = paddingValues,
        )
    }
}
