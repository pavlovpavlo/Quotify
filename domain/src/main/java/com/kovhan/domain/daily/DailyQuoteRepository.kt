package com.kovhan.domain.daily

import com.kovhan.core.models.quote.DailyQuote

interface DailyQuoteRepository {
    suspend fun prefetch()

    suspend fun getDailyQuote(): DailyQuote?

    /**
     * Today's already-chosen quote, read from the local cache only. Unlike
     * [getDailyQuote] this never fetches and never picks — callers that merely
     * display the quote (the widget) must not mutate the day's selection or
     * block on the network.
     */
    suspend fun getCachedDailyQuote(): DailyQuote?

    suspend fun dismissForToday()
}
