package com.kovhan.domain.daily.use_case

import com.kovhan.domain.daily.DailyQuoteRepository
import javax.inject.Inject

class DismissDailyQuoteForTodayUseCase @Inject constructor(
    private val repository: DailyQuoteRepository,
) {
    suspend operator fun invoke() = repository.dismissForToday()
}
