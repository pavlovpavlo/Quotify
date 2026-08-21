package com.kovhan.core.models.survey

/**
 * A questionnaire resolved for one language. Everything the survey renders comes
 * from here — the invite copy, the questions and the reward — so a survey can be
 * added or reworded from Remote Config without an app release.
 */
data class Survey(
    val id: String,
    val enabled: Boolean = true,
    val showProgress: Boolean = true,
    val invite: SurveyInvite? = null,
    val reward: SurveyReward? = null,
    val questions: List<SurveyQuestion> = emptyList(),
)

data class SurveyInvite(
    val title: String? = null,
    val body: String? = null,
    val reward: String? = null,
)

data class SurveyReward(
    val id: String,
    val kind: SurveyRewardKind,
)

enum class SurveyRewardKind {
    WIDGET_BACKGROUND,
    NONE;

    companion object {
        fun fromRaw(raw: String?): SurveyRewardKind = when (raw?.lowercase()) {
            "widget_background" -> WIDGET_BACKGROUND
            else -> NONE
        }
    }
}
