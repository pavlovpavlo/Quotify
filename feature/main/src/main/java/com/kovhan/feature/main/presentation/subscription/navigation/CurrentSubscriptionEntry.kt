package com.kovhan.feature.main.presentation.subscription.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.PaywallKey
import com.kovhan.feature.main.presentation.subscription.CurrentSubscriptionScreen
import com.kovhan.feature.main.presentation.subscription.CurrentSubscriptionViewModel

@Composable
internal fun CurrentSubscriptionEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<CurrentSubscriptionViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    CurrentSubscriptionScreen(
        state = state.value,
        onBack = { coordinator.goBack() },
        onRefresh = viewModel::onRefreshClicked,
        onOpenPaywall = { coordinator.navigate(PaywallKey) },
        paddingValues = paddingValues,
    )
}
