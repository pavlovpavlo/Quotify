package com.kovhan.domain.widget

/**
 * The widget snapshot mixes two id namespaces: library quotes (plain id) and the
 * quote of the day, which lives in its own table. Daily entries are prefixed so
 * one snapshot list can carry both.
 */
object WidgetQuoteRef {

    private const val DAILY_PREFIX = "daily:"

    fun daily(dailyQuoteId: String): String = DAILY_PREFIX + dailyQuoteId

    fun isDaily(ref: String): Boolean = ref.startsWith(DAILY_PREFIX)

    fun dailyId(ref: String): String = ref.removePrefix(DAILY_PREFIX)
}
