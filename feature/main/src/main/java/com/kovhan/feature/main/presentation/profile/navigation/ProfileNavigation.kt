package com.kovhan.feature.main.presentation.profile.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kovhan.core.ui.navigation.MainGraph
import com.kovhan.feature.main.presentation.profile.ProfileScreen
import com.kovhan.feature.main.presentation.profile.ProfileScreenViewModel
import com.kovhan.feature.main.presentation.profile.mvi.ProfileScreenEffect

internal fun NavGraphBuilder.profileScreen(
    navAction: ProfileScreenNavAction,
    paddingValues: PaddingValues,
) {
    composable<MainGraph.ProfileScreen> {
        val viewModel = hiltViewModel<ProfileScreenViewModel>()
        val state = viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    ProfileScreenEffect.NavigateToAuth -> navAction.navigateToAuth()
                }
            }
        }

        ProfileScreen(
            state = state.value,
            intent = viewModel,
            navAction = navAction,
            paddingValues = paddingValues,
        )
    }
}
