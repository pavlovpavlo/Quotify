package com.kovhan.core.models.survey

data class SurveyResponse(
    val surveyId: String,
    val answers: List<SurveyAnswer>,
    val completed: Boolean,
    val language: String,
)

/**
 * Carries the wording next to the ids: the stored answer stays readable on its
 * own, which is what makes the exported responses analysable later.
 */
data class SurveyAnswer(
    val questionId: String,
    val questionTitle: String,
    val optionIds: List<String>,
    val optionTexts: List<String>,
    /** Free text keyed by the option that asked for it. */
    val inputs: Map<String, String> = emptyMap(),
)
