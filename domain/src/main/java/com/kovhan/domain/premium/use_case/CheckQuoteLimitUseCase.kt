package com.kovhan.domain.premium.use_case

import com.kovhan.domain.billing.use_case.IsSubscribedUseCase
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.premium.PremiumLimits
import javax.inject.Inject


class CheckQuoteLimitUseCase @Inject constructor(
    private val isSubscribed: IsSubscribedUseCase,
    private val quoteRepository: QuoteRepository,
) {
    suspend operator fun invoke(): Boolean {
        if (isSubscribed()) return true
        return quoteRepository.count() < PremiumLimits.FREE_MAX_QUOTES
    }
}
