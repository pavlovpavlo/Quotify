package com.kovhan.feature.addquote.presentation.analytics

import com.kovhan.core.analytics.InputMethod
import com.kovhan.core.navigation.models.QuoteInputMethod

internal fun QuoteInputMethod.toAnalytics(): InputMethod = when (this) {
    QuoteInputMethod.TEXT -> InputMethod.TEXT
    QuoteInputMethod.CAMERA -> InputMethod.CAMERA
    QuoteInputMethod.VOICE -> InputMethod.VOICE
}
