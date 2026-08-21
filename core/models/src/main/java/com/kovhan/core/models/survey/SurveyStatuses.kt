package com.kovhan.core.models.survey

/**
 * Which surveys the user is done with. [postponed] maps a survey id to the
 * moment "Later" was tapped, so the invite can come back after a cooldown.
 */
data class SurveyStatuses(
    val completed: Set<String> = emptySet(),
    val skipped: Set<String> = emptySet(),
    val postponed: Map<String, Long> = emptyMap(),
) {
    fun merge(other: SurveyStatuses): SurveyStatuses = SurveyStatuses(
        completed = completed + other.completed,
        skipped = skipped + other.skipped,
        postponed = postponed + other.postponed,
    )

    fun isSettled(surveyId: String): Boolean =
        surveyId in completed || surveyId in skipped

    fun isPostponedWithin(surveyId: String, cooldownMs: Long, now: Long): Boolean {
        val postponedAt = postponed[surveyId] ?: return false
        return now - postponedAt < cooldownMs
    }
}
