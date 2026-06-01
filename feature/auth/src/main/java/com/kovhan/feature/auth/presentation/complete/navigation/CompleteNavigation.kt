package com.kovhan.feature.auth.presentation.complete.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kovhan.core.ui.navigation.CompleteGraph
import com.kovhan.feature.auth.presentation.complete.CompleteScreen
import com.kovhan.feature.auth.presentation.complete.CompleteScreenViewModel
import com.kovhan.feature.auth.presentation.complete.mvi.CompleteScreenEffect

internal fun NavGraphBuilder.completeScreen(
    navAction: CompleteScreenNavAction,
    paddingValues: PaddingValues,
) {
    composable<CompleteGraph.CompleteScreen> {
        val viewModel = hiltViewModel<CompleteScreenViewModel>()
        val state by viewModel.uiState.collectAsStateWithLifecycle()

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
            state = state,
            intent = viewModel,
            navAction = navAction,
            paddingValues = paddingValues,
        )
    }
}
