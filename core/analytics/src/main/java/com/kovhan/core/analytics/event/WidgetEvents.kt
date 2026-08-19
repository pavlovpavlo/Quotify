package com.kovhan.core.analytics.event

import com.kovhan.core.analytics.AnalyticsEvent
import com.kovhan.core.analytics.AnalyticsParam
import com.kovhan.core.analytics.FeedbackReason
import com.kovhan.core.analytics.FeedbackResult
import com.kovhan.core.analytics.WidgetQuoteSource
import com.kovhan.core.analytics.WidgetStyleName
import com.kovhan.core.analytics.WidgetTextAlignment
import com.kovhan.core.analytics.WidgetTextColour

data class WidgetSnapshot(
    val style: WidgetStyleName,
    val backgroundId: String,
    val backgroundColourId: String,
    val backgroundBlur: Boolean,
    val border: Boolean,
    val borderColourId: String,
    val fontSize: Int,
    val textColour: WidgetTextColour,
    val textAlignment: WidgetTextAlignment,
    val quoteSource: WidgetQuoteSource,
    val addDailyQuote: Boolean,
    val refreshHours: Int,
)

private fun WidgetSnapshot.toParams(): Map<String, Any> = mapOf(
    AnalyticsParam.WIDGET_TYPE to style.value,
    AnalyticsParam.BACKGROUND to backgroundId,
    AnalyticsParam.BACKGROUND_COLOUR to backgroundColourId,
    AnalyticsParam.BACKGROUND_BLUR to backgroundBlur,
    AnalyticsParam.BORDER to border,
    AnalyticsParam.BORDER_COLOUR to borderColourId,
    AnalyticsParam.FONT_SIZE to fontSize,
    AnalyticsParam.TEXT_COLOUR to textColour.value,
    AnalyticsParam.TEXT_ALIGNMENT to textAlignment.value,
    AnalyticsParam.QUOTE_SOURCE to quoteSource.value,
    AnalyticsParam.ADD_DAILY_QUOTE to addDailyQuote,
    AnalyticsParam.REFRESH to refreshHours,
)

class WidgetAdded(snapshot: WidgetSnapshot) : AnalyticsEvent(
    name = "widget_added",
    params = snapshot.toParams(),
)

class WidgetRemoved(hadWidgetSettings: Boolean) : AnalyticsEvent(
    name = "widget_removed",
    params = mapOf(AnalyticsParam.WIDGET_ADDED to hadWidgetSettings),
)

class WidgetUpdated(
    snapshot: WidgetSnapshot,
    from: WidgetQuoteSource,
    to: WidgetQuoteSource,
) : AnalyticsEvent(
    name = "widget_updated",
    params = snapshot.toParams() + mapOf(
        AnalyticsParam.FROM to from.value,
        AnalyticsParam.TO to to.value,
    ),
)

data object OfflineShown : AnalyticsEvent(name = "offline_shown")

class FeedbackSubmitted(
    reason: FeedbackReason,
    result: FeedbackResult,
    hasComment: Boolean,
) : AnalyticsEvent(
    name = "feedback_submited",
    params = mapOf(
        AnalyticsParam.REASON to reason.value,
        AnalyticsParam.RESULT to result.value,
        AnalyticsParam.TEXT to hasComment,
    ),
)

data object RateUsInitiated : AnalyticsEvent(name = "rate_us_initiated")
