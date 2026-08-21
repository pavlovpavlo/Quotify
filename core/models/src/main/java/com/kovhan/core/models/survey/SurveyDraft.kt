package com.kovhan.core.models.survey

/**
 * Partial progress kept while the user is away, so leaving the questionnaire
 * mid-way and coming back resumes on the same step.
 */
data class SurveyDraft(
    val surveyId: String,
    val stepIndex: Int = 0,
    val answers: Map<String, Set<String>> = emptyMap(),
    val inputs: Map<String, Map<String, String>> = emptyMap(),
)
