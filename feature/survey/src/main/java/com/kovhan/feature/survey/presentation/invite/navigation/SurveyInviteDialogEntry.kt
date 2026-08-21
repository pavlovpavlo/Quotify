package com.kovhan.feature.survey.presentation.invite.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.SurveyInviteAction
import com.kovhan.core.navigation.SurveyInviteDialogKey
import com.kovhan.core.navigation.SurveyKey
import com.kovhan.feature.survey.presentation.invite.SurveyInviteDialog
import com.kovhan.feature.survey.presentation.invite.SurveyInviteViewModel
import kotlinx.coroutines.launch

@Composable
internal fun SurveyInviteDialogEntry(
    key: SurveyInviteDialogKey,
    coordinator: NavigationCoordinator,
) {
    val viewModel = hiltViewModel<SurveyInviteViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key.surveyId) {
        viewModel.initialize(key.surveyId)
    }

    fun finish(action: SurveyInviteAction, onFinished: () -> Unit = {}) {
        coroutineScope.launch {
            coordinator.dismissDialogWithResult(
                NavigationCoordinator.KEY_SURVEY_INVITE,
                action,
            )
            onFinished()
        }
    }

    SurveyInviteDialog(
        invite = state.value.invite,
        onStart = {
            viewModel.onStart()
            finish(SurveyInviteAction.START) {
                coordinator.navigate(SurveyKey(key.surveyId))
            }
        },
        onLater = {
            viewModel.onLater()
            finish(SurveyInviteAction.LATER)
        },
        onDismiss = {
            viewModel.onDismiss()
            finish(SurveyInviteAction.DISMISS)
        },
    )
}
