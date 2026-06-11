package com.kovhan.feature.main.presentation.favorites.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.feature.main.presentation.favorites.FavoritesScreen
import com.kovhan.feature.main.presentation.favorites.FavoritesScreenViewModel

@Composable
internal fun FavoritesEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<FavoritesScreenViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    FavoritesScreen(
        state = state.value,
        intent = viewModel,
        navAction = object : FavoritesScreenNavAction {
            override fun navigateBack() {
                coordinator.goBack()
            }

            override fun navigateToQuoteDetails(quoteId: String) = Unit
        },
        paddingValues = paddingValues,
    )
}
