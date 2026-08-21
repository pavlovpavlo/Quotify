package com.kovhan.feature.survey.presentation.survey.mvi

import com.kovhan.core.models.survey.Survey
import com.kovhan.core.models.survey.SurveyQuestion
import com.kovhan.core.ui.UiState
import com.kovhan.feature.survey.presentation.done.SurveyRewardState

data class SurveyState(
    val survey: Survey? = null,
    val isLoading: Boolean = true,
    val stepIndex: Int = 0,
    val answers: Map<String, Set<String>> = emptyMap(),
    val inputs: Map<String, Map<String, String>> = emptyMap(),
    val isDone: Boolean = false,
    val reward: SurveyRewardState = SurveyRewardState.None,
) : UiState {

    val questions: List<SurveyQuestion> get() = survey?.questions.orEmpty()

    val question: SurveyQuestion? get() = questions.getOrNull(stepIndex)

    val showProgress: Boolean get() = survey?.showProgress == true

    val stepNumber: Int get() = stepIndex + 1

    val isLastStep: Boolean get() = stepIndex >= questions.lastIndex

    val canGoBack: Boolean get() = stepIndex > 0

    val canSkip: Boolean get() = question?.skippable == true

    /**
     * Next is enabled once something is picked and every required extra field
     * behind a picked option is filled in.
     */
    val canAdvance: Boolean
        get() {
            val current = question ?: return false
            val selected = selectedOptions(current.id)
            if (selected.isEmpty()) return false

            return current.options
                .filter { it.id in selected && it.input?.required == true }
                .all { input(current.id, it.id).isNotBlank() }
        }

    fun selectedOptions(questionId: String): Set<String> = answers[questionId].orEmpty()

    fun input(questionId: String, optionId: String): String =
        inputs[questionId]?.get(optionId).orEmpty()

    fun questionInputs(questionId: String): Map<String, String> = inputs[questionId].orEmpty()
}
