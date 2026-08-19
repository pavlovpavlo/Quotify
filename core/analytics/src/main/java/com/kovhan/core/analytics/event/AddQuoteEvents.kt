package com.kovhan.core.analytics.event

import com.kovhan.core.analytics.AddQuoteSource
import com.kovhan.core.analytics.AddQuoteStep
import com.kovhan.core.analytics.AiFeatureName
import com.kovhan.core.analytics.AnalyticsEvent
import com.kovhan.core.analytics.AnalyticsParam
import com.kovhan.core.analytics.CameraFailure
import com.kovhan.core.analytics.InputMethod
import com.kovhan.core.analytics.PermissionResult
import com.kovhan.core.analytics.PermissionType
import com.kovhan.core.analytics.VoiceFailure

class AddQuoteInitiated(inputMethod: InputMethod, source: AddQuoteSource) : AnalyticsEvent(
    name = "add_quote_initiated",
    params = mapOf(
        AnalyticsParam.INPUT_METHOD to inputMethod.value,
        AnalyticsParam.REASON to source.value,
    ),
)

class ScanCompleted(textLength: Int, durationSeconds: Long) : AnalyticsEvent(
    name = "scan_completed",
    params = mapOf(
        AnalyticsParam.TEXT_LENGTH to textLength,
        AnalyticsParam.DURATION_SECONDS to durationSeconds,
    ),
)

class ScanCompletedEdit(edited: Boolean) : AnalyticsEvent(
    name = "scan_completed_edit",
    params = mapOf(AnalyticsParam.EDIT to edited),
)

class VoiceCompleted(textLength: Int) : AnalyticsEvent(
    name = "voice_completed",
    params = mapOf(AnalyticsParam.TEXT_LENGTH to textLength),
)

class VoiceCompletedEdit(edited: Boolean) : AnalyticsEvent(
    name = "voice_completed_edit",
    params = mapOf(AnalyticsParam.EDIT to edited),
)

class CameraFailed(reason: CameraFailure) : AnalyticsEvent(
    name = "camera_failed",
    params = mapOf(AnalyticsParam.REASON to reason.value),
)

class VoiceFailed(reason: VoiceFailure) : AnalyticsEvent(
    name = "voice_failed",
    params = mapOf(AnalyticsParam.REASON to reason.value),
)

class PermissionResultEvent(
    permission: PermissionType,
    result: PermissionResult,
) : AnalyticsEvent(
    name = "permission_result",
    params = mapOf(
        AnalyticsParam.PERMISSION to permission.value,
        AnalyticsParam.RESULT to result.value,
    ),
)

class QuoteAdded(
    inputMethod: InputMethod,
    textLength: Int,
    hasAuthor: Boolean,
    hasBook: Boolean,
    tagsCount: Int,
    widgetEnabled: Boolean,
    pushEnabled: Boolean,
) : AnalyticsEvent(
    name = "quote_added",
    params = mapOf(
        AnalyticsParam.INPUT_METHOD to inputMethod.value,
        AnalyticsParam.TEXT_LENGTH to textLength,
        AnalyticsParam.HAS_AUTHOR to hasAuthor,
        AnalyticsParam.HAS_BOOK to hasBook,
        AnalyticsParam.TAGS_COUNT to tagsCount,
        AnalyticsParam.WIDGET_ENABLED to widgetEnabled,
        AnalyticsParam.PUSH_ENABLED to pushEnabled,
    ),
)

class QuoteAddClosed(step: AddQuoteStep) : AnalyticsEvent(
    name = "quote_add_closed",
    params = mapOf(AnalyticsParam.STEP to step.value),
)

data object AiTagsRequested : AnalyticsEvent(name = "ai_tags_requested")

class AiTagsAccepted(suggestedCount: Int, acceptedCount: Int) : AnalyticsEvent(
    name = "ai_tags_accepted",
    params = mapOf(
        AnalyticsParam.SUGGESTED_TAGS_COUNT to suggestedCount,
        AnalyticsParam.ACCEPTED_TAGS_COUNT to acceptedCount,
    ),
)

class AiLimitReached(feature: AiFeatureName) : AnalyticsEvent(
    name = "ai_limit_reached",
    params = mapOf(AnalyticsParam.REASON to feature.value),
)
