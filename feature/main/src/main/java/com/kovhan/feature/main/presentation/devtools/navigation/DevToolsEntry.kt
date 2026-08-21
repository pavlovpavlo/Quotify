package com.kovhan.feature.main.presentation.devtools.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.OfferKey
import com.kovhan.core.navigation.SurveyKey
import com.kovhan.core.navigation.SurveyPlatePreviewKey
import com.kovhan.feature.main.presentation.devtools.DevToolsScreen
import com.kovhan.feature.main.presentation.devtools.DevToolsViewModel

@Composable
internal fun DevToolsEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<DevToolsViewModel>()

    DevToolsScreen(
        onBack = coordinator::goBack,
        onOpenOffer = { coordinator.navigate(OfferKey()) },
        onOpenSurvey = { coordinator.navigate(SurveyKey()) },
        onOpenSurveyPlate = { coordinator.navigate(SurveyPlatePreviewKey) },
        onResetSurveyState = viewModel::onResetSurveyStateClicked,
        onOpenDialog = coordinator::showDialog,
        paddingValues = paddingValues,
    )
}
