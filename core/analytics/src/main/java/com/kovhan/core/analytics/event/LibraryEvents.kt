package com.kovhan.core.analytics.event

import com.kovhan.core.analytics.AnalyticsEvent
import com.kovhan.core.analytics.AnalyticsParam
import com.kovhan.core.analytics.CollectionAction
import com.kovhan.core.analytics.CollectionCreateSource
import com.kovhan.core.analytics.DailyQuoteResult
import com.kovhan.core.analytics.QuoteAction

data object VisitLibrary : AnalyticsEvent(name = "visit_library")

data object VisitProfile : AnalyticsEvent(name = "visit_profile")

class CollectionCreated(source: CollectionCreateSource) : AnalyticsEvent(
    name = "collection_created",
    params = mapOf(AnalyticsParam.REASON to source.value),
)

class VisitCollection(name: String, quoteCount: Int) : AnalyticsEvent(
    name = "visit_collection",
    params = mapOf(
        AnalyticsParam.NAME to name,
        AnalyticsParam.QUOTE_COUNT to quoteCount,
    ),
)

class CollectionSettings(
    action: CollectionAction,
    changedName: Boolean? = null,
    changedIcon: Boolean? = null,
    changedColor: Boolean? = null,
    changeFrom: String? = null,
    changeTo: String? = null,
) : AnalyticsEvent(
    name = "collection_settings",
    params = buildMap {
        put(AnalyticsParam.REASON, action.value)
        changedName?.let { put(AnalyticsParam.CHANGED_NAME, it) }
        changedIcon?.let { put(AnalyticsParam.CHANGED_ICON, it) }
        changedColor?.let { put(AnalyticsParam.CHANGED_COLOR, it) }
        changeFrom?.let { put(AnalyticsParam.CHANGE_FROM, it) }
        changeTo?.let { put(AnalyticsParam.CHANGE_TO, it) }
    },
)

class QuoteSettings(action: QuoteAction) : AnalyticsEvent(
    name = "quote_settings",
    params = mapOf(AnalyticsParam.REASON to action.value),
)

data object SearchInitiated : AnalyticsEvent(name = "search_initiated")

class HideDailyQuoteInitiated(quoteId: String) : AnalyticsEvent(
    name = "hide_daily_quote_initiated",
    params = mapOf(AnalyticsParam.QUOTE_ID to quoteId),
)

class HideDailyQuoteFinished(quoteId: String, result: DailyQuoteResult) : AnalyticsEvent(
    name = "hide_daily_quote_finished",
    params = mapOf(
        AnalyticsParam.QUOTE_ID to quoteId,
        AnalyticsParam.RESULT to result.value,
    ),
)

class DailyQuoteSaved(quoteId: String) : AnalyticsEvent(
    name = "daily_quote_saved",
    params = mapOf(AnalyticsParam.QUOTE_ID to quoteId),
)
