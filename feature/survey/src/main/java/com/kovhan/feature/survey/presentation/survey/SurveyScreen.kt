package com.kovhan.feature.survey.presentation.survey

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.survey.presentation.done.SurveyDoneContent
import com.kovhan.feature.survey.presentation.survey.mvi.SurveyIntent
import com.kovhan.feature.survey.presentation.survey.mvi.SurveyState

@Composable
internal fun SurveyScreen(
    state: SurveyState,
    intent: SurveyIntent,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val focusManager = LocalFocusManager.current

    BackHandler { if (state.isDone) intent.onExitClicked() else intent.onBackClicked() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.bgPrimary)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            )
            .imePadding(),
    ) {
        val question = state.question

        when {
            state.isLoading -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = colors.accentPrimary,
            )

            state.isDone || question == null -> SurveyDoneContent(
                reward = state.reward,
                onPrimaryClick = intent::onRewardPrimaryClicked,
                onSecondaryClick = intent::onRewardSecondaryClicked,
            )

            else -> SurveyQuestionContent(
                state = state,
                question = question,
                intent = intent,
            )
        }
    }
}
