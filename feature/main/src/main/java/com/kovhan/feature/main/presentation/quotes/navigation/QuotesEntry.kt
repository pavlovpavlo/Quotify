package com.kovhan.feature.main.presentation.quotes.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.HideDailyQuoteDialogKey
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

    LaunchedEffect(Unit) {
        coordinator.observeResult<Boolean>(NavigationCoordinator.KEY_DAILY_QUOTE_HIDE_FOREVER)
            .collect { confirmed -> if (confirmed == true) viewModel.onHideDailyQuoteForever() }
    }
    LaunchedEffect(Unit) {
        coordinator.observeResult<Boolean>(NavigationCoordinator.KEY_DAILY_QUOTE_HIDE_TODAY)
            .collect { confirmed -> if (confirmed == true) viewModel.onHideDailyQuoteToday() }
    }

    QuotesScreen(
        state = state.value,
        intent = viewModel,
        navAction = object : QuotesScreenNavAction {
            override fun onBack() {
                coordinator.goBack()
            }

            override fun navigateToQuoteDetails(quoteId: String) = Unit

            override fun showHideDailyQuoteDialog() =
                coordinator.showDialog(HideDailyQuoteDialogKey)
        },
        paddingValues = paddingValues,
    )
}
