package com.kovhan.feature.main.presentation.devtools.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.OfferKey
import com.kovhan.feature.main.presentation.devtools.DevToolsScreen

@Composable
internal fun DevToolsEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    DevToolsScreen(
        onBack = coordinator::goBack,
        onOpenOffer = { coordinator.navigate(OfferKey) },
        paddingValues = paddingValues,
    )
}
