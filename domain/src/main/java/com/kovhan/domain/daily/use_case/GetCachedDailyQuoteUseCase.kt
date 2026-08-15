package com.kovhan.domain.daily.use_case

import com.kovhan.core.models.quote.DailyQuote
import com.kovhan.domain.daily.DailyQuoteRepository
import javax.inject.Inject

class GetCachedDailyQuoteUseCase @Inject constructor(
    private val repository: DailyQuoteRepository,
) {
    suspend operator fun invoke(): DailyQuote? = repository.getCachedDailyQuote()
}
