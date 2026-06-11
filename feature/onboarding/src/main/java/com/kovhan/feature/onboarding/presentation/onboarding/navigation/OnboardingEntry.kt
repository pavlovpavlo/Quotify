package com.kovhan.feature.onboarding.presentation.onboarding.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.CompleteKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.feature.onboarding.presentation.onboarding.OnboardingScreen
import com.kovhan.feature.onboarding.presentation.onboarding.OnboardingScreenViewModel
import com.kovhan.feature.onboarding.presentation.onboarding.mvi.OnboardingScreenEffect

@Composable
internal fun OnboardingEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<OnboardingScreenViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    val navAction = object : OnboardingScreenNavAction {
        override fun onBack() {
            coordinator.goBack()
        }

        override fun navigateToComplete() {
            coordinator.navigate(CompleteKey)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is OnboardingScreenEffect.GoToPage -> Unit
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
