package com.kovhan.feature.subscription.presentation.offer.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.WebViewKey
import com.kovhan.feature.subscription.presentation.offer.OfferScreen
import com.kovhan.feature.subscription.presentation.offer.OfferViewModel

@Composable
internal fun OfferEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<OfferViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.purchaseSucceeded) {
        if (state.purchaseSucceeded) coordinator.goBack()
    }

    LaunchedEffect(state.isLoading, state.offer) {
        if (!state.isLoading && state.offer == null) coordinator.goBack()
    }

    OfferScreen(
        state = state,
        intent = viewModel,
        onClose = coordinator::goBack,
        onOpenLink = { title, url -> coordinator.navigate(WebViewKey(title = title, url = url)) },
        paddingValues = paddingValues,
    )
}
