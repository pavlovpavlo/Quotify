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

    /**
     * Сьогоднішня цитата дня для фонових споживачів: перевикористовує вибір за
     * сьогодні, а якщо його ще нема — робить новий із локального пулу. На
     * відміну від [getDailyQuote] не ходить у мережу, тож придатна для віджета,
     * який мусить оживати й без запуску застосунку.
     */
    suspend fun ensureDailyQuote(): DailyQuote?

    /** Ховає картку в бібліотеці до кінця доби. На віджет не впливає. */
    suspend fun dismissForToday()
}
