package com.kovhan.feature.main.presentation.quotes.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.feature.main.presentation.quotes.QuotesScreen
import com.kovhan.feature.main.presentation.quotes.QuotesScreenViewModel

@Composable
internal fun QuotesEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<QuotesScreenViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    QuotesScreen(
        state = state.value,
        intent = viewModel,
        navAction = object : QuotesScreenNavAction {
            override fun onBack() {
                coordinator.goBack()
            }

            override fun navigateToQuoteDetails(quoteId: String) = Unit
        },
        paddingValues = paddingValues,
    )
}
