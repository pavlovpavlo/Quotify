package com.kovhan.domain.daily

import com.kovhan.core.models.DailyQuote

interface DailyQuoteRepository {
    suspend fun prefetch()

    suspend fun getDailyQuote(): DailyQuote?

    suspend fun dismissForToday()
}
