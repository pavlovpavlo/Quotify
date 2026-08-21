package com.kovhan.core.models.widget

/**
 * Reward covers handed out for finished surveys — one per completed survey, in
 * order. They live apart from [WidgetCovers] because they are earned, not
 * offered to everyone.
 */
object WidgetSpecialCovers {

    private const val COUNT = 10

    val ALL: List<String> = List(COUNT) { index -> "special_${index + 1}" }

    /** Covers the user has already earned. */
    fun unlocked(completedSurveys: Int): List<String> =
        ALL.take(completedSurveys.coerceIn(0, ALL.size))

    /** The cover granted by the [completedSurveys]-th survey, or null once they run out. */
    fun rewardFor(completedSurveys: Int): String? = ALL.getOrNull(completedSurveys - 1)

    fun isSpecial(coverId: String): Boolean = coverId in ALL
}
