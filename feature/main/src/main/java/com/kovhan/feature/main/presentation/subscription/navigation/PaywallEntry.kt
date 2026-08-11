package com.kovhan.feature.main.presentation.subscription.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.feature.main.presentation.subscription.PaywallTestScreen
import com.kovhan.feature.main.presentation.subscription.PaywallTestViewModel

@Composable
internal fun PaywallEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<PaywallTestViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    PaywallTestScreen(
        state = state.value,
        onBack = { coordinator.goBack() },
        onRestore = viewModel::onRestoreClicked,
        onReload = viewModel::loadProduct,
        onBuy = viewModel::onBuyClicked,
        paddingValues = paddingValues,
    )
}
