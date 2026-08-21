package com.kovhan.feature.survey.presentation.survey

import com.kovhan.core.analytics.AnalyticsTracker
import com.kovhan.core.analytics.event.SurveyAbandoned
import com.kovhan.core.analytics.event.SurveyCompleted
import com.kovhan.core.analytics.event.SurveyQuestionAnswered
import com.kovhan.core.analytics.event.SurveyQuestionSkipped
import com.kovhan.core.analytics.event.SurveyResumed
import com.kovhan.core.analytics.event.SurveyRewardDepleted
import com.kovhan.core.analytics.event.SurveyRewardGranted
import com.kovhan.core.analytics.SurveyExitChoice
import com.kovhan.core.analytics.event.SurveyStepBack
import com.kovhan.core.analytics.event.SurveyStarted
import com.kovhan.core.models.survey.SurveyAnswer
import com.kovhan.core.models.survey.SurveyDraft
import com.kovhan.core.models.survey.SurveyQuestionType
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.survey.use_case.ClearSurveyDraftUseCase
import com.kovhan.domain.survey.use_case.GetSurveyDraftUseCase
import com.kovhan.domain.survey.use_case.GetSurveyUseCase
import com.kovhan.domain.survey.use_case.GrantSurveyRewardUseCase
import com.kovhan.domain.survey.use_case.MarkSurveyPostponedUseCase
import com.kovhan.domain.survey.use_case.MarkSurveySkippedUseCase
import com.kovhan.domain.survey.use_case.SaveSurveyDraftUseCase
import com.kovhan.domain.survey.use_case.SubmitSurveyResponseUseCase
import com.kovhan.feature.survey.catalog.SurveyDemoCatalog
import com.kovhan.feature.survey.presentation.done.SurveyRewardState
import com.kovhan.feature.survey.presentation.survey.mvi.SurveyEffect
import com.kovhan.feature.survey.presentation.survey.mvi.SurveyIntent
import com.kovhan.feature.survey.presentation.survey.mvi.SurveyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @Inject constructor(
    private val getSurvey: GetSurveyUseCase,
    private val getDraft: GetSurveyDraftUseCase,
    private val saveDraft: SaveSurveyDraftUseCase,
    private val clearDraft: ClearSurveyDraftUseCase,
    private val submitResponse: SubmitSurveyResponseUseCase,
    private val grantReward: GrantSurveyRewardUseCase,
    private val markPostponed: MarkSurveyPostponedUseCase,
    private val markSkipped: MarkSurveySkippedUseCase,
    private val demoCatalog: SurveyDemoCatalog,
    private val analytics: AnalyticsTracker,
) : BaseViewModel<SurveyState, SurveyEffect>(SurveyState()), SurveyIntent {

    private var surveyId: String? = null

    fun initialize(surveyId: String) {
        if (this.surveyId == surveyId) return
        this.surveyId = surveyId

        viewModelScope.launch {
            val survey = getSurvey(surveyId) ?: demoCatalog.byId(surveyId)
            if (survey == null) {
                publishState { copy(isLoading = false) }
                publishEffect(SurveyEffect.Exit)
                return@launch
            }

            val draft = getDraft(surveyId)
            val resumedStep = draft?.stepIndex?.coerceIn(0, survey.questions.lastIndex) ?: 0

            analytics.track(SurveyStarted(surveyId))
            if (resumedStep > 0) analytics.track(SurveyResumed(surveyId, resumedStep + 1))

            publishState {
                SurveyState(
                    survey = survey,
                    isLoading = false,
                    stepIndex = resumedStep,
                    answers = draft?.answers.orEmpty(),
                    inputs = draft?.inputs.orEmpty(),
                )
            }
        }
    }

    override fun onOptionClicked(questionId: String, optionId: String) {
        val type = uiState.value.questions.firstOrNull { it.id == questionId }?.type ?: return

        publishState {
            val current = selectedOptions(questionId)
            val updated = when (type) {
                SurveyQuestionType.SINGLE -> setOf(optionId)
                SurveyQuestionType.MULTI ->
                    if (optionId in current) current - optionId else current + optionId
            }
            copy(
                answers = answers + (questionId to updated),
                inputs = inputs + (questionId to questionInputs(questionId).filterKeys { it in updated }),
            )
        }
    }

    override fun onInputChanged(questionId: String, optionId: String, text: String) =
        publishState {
            copy(inputs = inputs + (questionId to questionInputs(questionId) + (optionId to text)))
        }

    override fun onNextClicked() {
        val state = uiState.value
        val question = state.question ?: return
        val selected = state.selectedOptions(question.id)

        analytics.track(
            SurveyQuestionAnswered(
                surveyId = state.survey?.id.orEmpty(),
                questionId = question.id,
                optionsCount = selected.size,
                hasComment = state.questionInputs(question.id).values.any { it.isNotBlank() },
            ),
        )
        advance()
    }

    override fun onSkipClicked() {
        val state = uiState.value
        val question = state.question ?: return

        analytics.track(SurveyQuestionSkipped(state.survey?.id.orEmpty(), question.id))
        publishState {
            copy(
                answers = answers - question.id,
                inputs = inputs - question.id,
            )
        }
        advance()
    }

    override fun onBackClicked() {
        if (!uiState.value.canGoBack) {
            onExitClicked()
            return
        }
        val state = uiState.value
        analytics.track(SurveyStepBack(state.survey?.id.orEmpty(), state.stepNumber))

        publishState { copy(stepIndex = stepIndex - 1) }
        persistDraft()
    }

    override fun onExitClicked() {
        val state = uiState.value
        if (state.isDone || state.survey == null) {
            publishEffect(SurveyEffect.Exit)
            return
        }
        publishEffect(SurveyEffect.ConfirmExit)
    }

    /** Опитування повернеться пізніше, відповіді лишаються в чернетці. */
    fun onExitLater() {
        val id = uiState.value.survey?.id ?: return
        track(SurveyExitChoice.LATER)
        persistDraft()
        viewModelScope.launch { markPostponed(id) }
        publishEffect(SurveyEffect.Exit)
    }

    /** Більше не пропонуємо — і нагороди за нього не буде. */
    fun onExitSkip() {
        val id = uiState.value.survey?.id ?: return
        track(SurveyExitChoice.SKIP)
        viewModelScope.launch {
            clearDraft(id)
            markSkipped(id)
        }
        publishEffect(SurveyEffect.Exit)
    }

    private fun track(choice: SurveyExitChoice) {
        val state = uiState.value
        analytics.track(SurveyAbandoned(state.survey?.id.orEmpty(), state.stepNumber, choice))
    }

    override fun onRewardPrimaryClicked() = publishEffect(SurveyEffect.OpenWidgetSettings)

    override fun onRewardSecondaryClicked() = publishEffect(SurveyEffect.Exit)

    private fun advance() {
        if (uiState.value.isLastStep) {
            finish()
            return
        }
        publishState { copy(stepIndex = stepIndex + 1) }
        persistDraft()
    }

    private fun finish() {
        val state = uiState.value
        val id = state.survey?.id ?: return
        val answers = state.toAnswers()

        publishState { copy(isDone = true) }

        viewModelScope.launch {
            submitResponse(surveyId = id, answers = answers, completed = true)
            clearDraft(id)
            analytics.track(SurveyCompleted(id, answers.size))

            val outcome = grantReward(id)
            val coverId = outcome.coverId
            if (coverId != null) {
                analytics.track(SurveyRewardGranted(id, coverId))
                publishState { copy(reward = SurveyRewardState.Granted(coverId)) }
            } else {
                analytics.track(SurveyRewardDepleted(id, outcome.completedSurveys))
                publishState { copy(reward = SurveyRewardState.Depleted) }
            }
        }
    }

    private fun persistDraft() {
        val state = uiState.value
        val id = state.survey?.id ?: return

        viewModelScope.launch {
            saveDraft(
                SurveyDraft(
                    surveyId = id,
                    stepIndex = state.stepIndex,
                    answers = state.answers,
                    inputs = state.inputs,
                ),
            )
        }
    }

    private fun SurveyState.toAnswers(): List<SurveyAnswer> = questions.mapNotNull { question ->
        val selected = selectedOptions(question.id)
        if (selected.isEmpty()) return@mapNotNull null

        val picked = question.options.filter { it.id in selected }

        SurveyAnswer(
            questionId = question.id,
            questionTitle = question.title,
            optionIds = picked.map { it.id },
            optionTexts = picked.map { it.text },
            inputs = questionInputs(question.id).filterValues { it.isNotBlank() },
        )
    }
}
