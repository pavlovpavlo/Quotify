package com.kovhan.feature.subscription.presentation.current.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.ManageSubscriptionSheetKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.PaywallKey
import com.kovhan.feature.subscription.presentation.current.CurrentSubscriptionScreen
import com.kovhan.feature.subscription.presentation.current.CurrentSubscriptionViewModel

@Composable
internal fun CurrentSubscriptionEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<CurrentSubscriptionViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CurrentSubscriptionScreen(
        state = state,
        onBack = coordinator::goBack,
        onManage = { coordinator.showBottomSheet(ManageSubscriptionSheetKey) },
        onOpenPaywall = { coordinator.navigate(PaywallKey) },
        paddingValues = paddingValues,
    )
}
