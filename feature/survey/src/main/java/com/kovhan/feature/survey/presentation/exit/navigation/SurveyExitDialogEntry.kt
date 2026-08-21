package com.kovhan.feature.survey.presentation.exit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.SurveyExitAction
import com.kovhan.feature.survey.presentation.exit.SurveyExitDialog
import kotlinx.coroutines.launch

@Composable
internal fun SurveyExitDialogEntry(coordinator: NavigationCoordinator) {
    val coroutineScope = rememberCoroutineScope()

    fun finish(action: SurveyExitAction?) {
        coroutineScope.launch {
            if (action == null) {
                coordinator.dismissDialog()
                return@launch
            }
            coordinator.dismissDialogWithResult(NavigationCoordinator.KEY_SURVEY_EXIT, action)
        }
    }

    SurveyExitDialog(
        onLater = { finish(SurveyExitAction.LATER) },
        onSkip = { finish(SurveyExitAction.SKIP) },
        onContinue = { finish(null) },
    )
}
