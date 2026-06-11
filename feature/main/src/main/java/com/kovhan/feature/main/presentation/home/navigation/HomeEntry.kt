package com.kovhan.feature.main.presentation.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.feature.main.presentation.home.HomeScreen
import com.kovhan.feature.main.presentation.home.HomeScreenViewModel

@Composable
internal fun HomeEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<HomeScreenViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        state = state.value,
        intent = viewModel,
        navAction = object : HomeScreenNavAction {
            override fun navigateBack() {
                coordinator.goBack()
            }
        },
        paddingValues = paddingValues,
    )
}
