package com.kovhan.feature.main.presentation.edit_profile.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kovhan.core.ui.navigation.MainGraph
import com.kovhan.core.ui.snackbar.SnackbarMessageEffect
import com.kovhan.feature.main.presentation.edit_profile.EditProfileScreen
import com.kovhan.feature.main.presentation.edit_profile.EditProfileViewModel
import com.kovhan.feature.main.presentation.edit_profile.mvi.EditProfileEffect

internal fun NavGraphBuilder.editProfileScreen(
    navAction: EditProfileScreenNavAction,
    paddingValues: PaddingValues,
) {
    composable<MainGraph.EditProfileScreen> {
        val viewModel = hiltViewModel<EditProfileViewModel>()
        val state = viewModel.uiState.collectAsStateWithLifecycle()
        val snackbarHostState = remember { SnackbarHostState() }

        SnackbarMessageEffect(viewModel.snackbar, snackbarHostState)

        LaunchedEffect(Unit) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    EditProfileEffect.NavigateToAuth -> navAction.navigateToAuth()
                }
            }
        }

        EditProfileScreen(
            state = state.value,
            intent = viewModel,
            navAction = navAction,
            paddingValues = paddingValues,
            snackbarHostState = snackbarHostState,
        )
    }
}
