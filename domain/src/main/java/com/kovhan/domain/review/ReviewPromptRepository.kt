package com.kovhan.domain.review

object ReviewPrompt {
    const val MIN_DAYS_INSTALLED = 7L
}

interface ReviewPromptRepository {

    suspend fun installedAt(): Long

    suspend fun isAsked(): Boolean

    suspend fun markAsked()
}
