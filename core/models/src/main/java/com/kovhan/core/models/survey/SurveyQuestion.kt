package com.kovhan.core.models.survey

data class SurveyQuestion(
    val id: String,
    val title: String,
    val description: String? = null,
    val type: SurveyQuestionType = SurveyQuestionType.SINGLE,
    val skippable: Boolean = true,
    val options: List<SurveyOption> = emptyList(),
)

data class SurveyOption(
    val id: String,
    val text: String,
    /** When set, picking this option reveals an extra free-text field. */
    val input: SurveyOptionInput? = null,
)

data class SurveyOptionInput(
    val required: Boolean = false,
    val label: String? = null,
    val placeholder: String? = null,
)

enum class SurveyQuestionType { SINGLE, MULTI }
