package com.kovhan.domain.ai

/**
 * Server-side counter of AI requests per user, keyed by day and month. Stored
 * remotely (not on-device) so the quota can't be reset by clearing app data.
 */
interface AiUsageRepository {
    suspend fun getUsage(): AiUsage

    suspend fun recordRequest()
}
