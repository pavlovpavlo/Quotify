package com.kovhan.feature.survey.presentation.survey

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kovhan.core.models.survey.SurveyQuestion
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.survey.presentation.survey.component.SurveyFooter
import com.kovhan.feature.survey.presentation.survey.component.SurveyOptionList
import com.kovhan.feature.survey.presentation.survey.component.SurveyProgressBar
import com.kovhan.feature.survey.presentation.survey.component.SurveyQuestionHeader
import com.kovhan.feature.survey.presentation.survey.component.SurveyTopBar
import com.kovhan.feature.survey.presentation.survey.mvi.SurveyIntent
import com.kovhan.feature.survey.presentation.survey.mvi.SurveyState

@Composable
internal fun SurveyQuestionContent(
    state: SurveyState,
    question: SurveyQuestion,
    intent: SurveyIntent,
    modifier: Modifier = Modifier,
) {
    val dimensions = QuotifyMaterialTheme.dimensions

    Column(modifier = modifier.fillMaxSize()) {
        SurveyTopBar(
            counter = if (state.showProgress) {
                stringResource(DsR.string.survey_step_of, state.stepNumber, state.questions.size)
            } else {
                null
            },
            showBack = state.canGoBack,
            onBack = intent::onBackClicked,
            onExit = intent::onExitClicked,
        )

        if (state.showProgress) {
            SurveyProgressBar(
                stepCount = state.questions.size,
                currentIndex = state.stepIndex,
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(
                    start = dimensions.size20,
                    end = dimensions.size20,
                    top = dimensions.size22,
                    bottom = dimensions.size12,
                ),
        ) {
            SurveyQuestionHeader(
                title = question.title,
                hint = question.description,
            )

            Spacer(Modifier.height(dimensions.size18))

            SurveyOptionList(
                question = question,
                selectedOptions = state.selectedOptions(question.id),
                inputs = state.questionInputs(question.id),
                onOptionClick = { optionId -> intent.onOptionClicked(question.id, optionId) },
                onInputChange = { optionId, text ->
                    intent.onInputChanged(question.id, optionId, text)
                },
            )
        }

        SurveyFooter(
            primaryText = stringResource(
                if (state.isLastStep) DsR.string.survey_finish else DsR.string.survey_next,
            ),
            primaryEnabled = state.canAdvance,
            onPrimaryClick = intent::onNextClicked,
            skipText = stringResource(DsR.string.survey_skip).takeIf { state.canSkip },
            onSkipClick = intent::onSkipClicked,
        )
    }
}
