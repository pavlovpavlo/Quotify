package com.kovhan.feature.auth.presentation.complete.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.LoginKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.QuotesKey
import com.kovhan.core.navigation.RegisterKey
import com.kovhan.feature.auth.presentation.complete.CompleteScreen
import com.kovhan.feature.auth.presentation.complete.CompleteScreenViewModel
import com.kovhan.feature.auth.presentation.complete.mvi.CompleteScreenEffect

@Composable
internal fun CompleteEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<CompleteScreenViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    val navAction = object : CompleteScreenNavAction {
        override fun navigateToSignUp() = coordinator.navigate(RegisterKey())
        override fun navigateToSignIn() = coordinator.navigate(LoginKey())
        override fun navigateToMain() = coordinator.navigateAndClearBackStack(QuotesKey)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                CompleteScreenEffect.NavigateToSignUp -> navAction.navigateToSignUp()
                CompleteScreenEffect.NavigateToSignIn -> navAction.navigateToSignIn()
                CompleteScreenEffect.NavigateToMain -> navAction.navigateToMain()
            }
        }
    }

    CompleteScreen(
        state = state.value,
        intent = viewModel,
        navAction = navAction,
        paddingValues = paddingValues,
    )
}
