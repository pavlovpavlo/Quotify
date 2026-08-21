package com.kovhan.feature.survey.presentation.plate.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.SurveyInviteDialogKey
import com.kovhan.feature.survey.presentation.plate.SurveyPlatePreviewScreen

@Composable
internal fun SurveyPlatePreviewEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    SurveyPlatePreviewScreen(
        onBack = coordinator::goBack,
        onPlateClick = { coordinator.showDialog(SurveyInviteDialogKey()) },
        paddingValues = paddingValues,
    )
}
