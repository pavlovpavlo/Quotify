package com.kovhan.feature.survey.presentation.survey.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.SurveyExitAction
import com.kovhan.core.navigation.SurveyExitDialogKey
import com.kovhan.core.navigation.SurveyKey
import com.kovhan.core.navigation.WidgetSettingsKey
import com.kovhan.feature.survey.presentation.survey.SurveyScreen
import com.kovhan.feature.survey.presentation.survey.SurveyViewModel
import com.kovhan.feature.survey.presentation.survey.mvi.SurveyEffect

@Composable
internal fun SurveyEntry(
    key: SurveyKey,
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<SurveyViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key.surveyId) {
        viewModel.initialize(key.surveyId)
    }

    LaunchedEffect(Unit) {
        coordinator.clearResult(NavigationCoordinator.KEY_SURVEY_EXIT)
        coordinator.observeResult<SurveyExitAction>(NavigationCoordinator.KEY_SURVEY_EXIT)
            .collect { action ->
                when (action) {
                    SurveyExitAction.LATER -> viewModel.onExitLater()
                    SurveyExitAction.SKIP -> viewModel.onExitSkip()
                    null -> Unit
                }
            }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                SurveyEffect.Exit -> coordinator.goBack()

                SurveyEffect.ConfirmExit ->
                    coordinator.showDialog(SurveyExitDialogKey(key.surveyId))

                SurveyEffect.OpenWidgetSettings -> {
                    coordinator.goBack()
                    coordinator.navigate(WidgetSettingsKey)
                }
            }
        }
    }

    SurveyScreen(
        state = state.value,
        intent = viewModel,
        paddingValues = paddingValues,
    )
}
