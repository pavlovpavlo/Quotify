package com.kovhan.feature.subscription.presentation.paywall.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.models.PaywallOrigin
import com.kovhan.core.navigation.WebViewKey
import com.kovhan.feature.subscription.presentation.common.toAnalytics
import com.kovhan.feature.subscription.presentation.paywall.PaywallScreen
import com.kovhan.feature.subscription.presentation.paywall.PaywallViewModel

@Composable
internal fun PaywallEntry(
    origin: PaywallOrigin,
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<PaywallViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(origin) {
        viewModel.onScreenOpened(origin.toAnalytics())
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.onScreenClosed() }
    }

    PaywallScreen(
        state = state,
        intent = viewModel,
        onClose = coordinator::goBack,
        onOpenLink = { title, url -> coordinator.navigate(WebViewKey(title = title, url = url)) },
        paddingValues = paddingValues,
    )
}
