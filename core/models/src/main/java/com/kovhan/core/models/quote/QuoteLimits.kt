package com.kovhan.core.models.quote

object QuoteLimits {
    const val MAX_TEXT_LENGTH = 1500

    fun normalizeText(text: String): String = text.trim().take(MAX_TEXT_LENGTH)
}
