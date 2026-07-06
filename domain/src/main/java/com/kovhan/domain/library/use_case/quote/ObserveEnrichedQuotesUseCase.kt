package com.kovhan.domain.library.use_case.quote

import com.kovhan.core.models.EnrichedQuote
import com.kovhan.core.models.QuoteFilter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveEnrichedQuotesUseCase @Inject constructor(
    private val observeFilteredQuotes: ObserveFilteredQuotesUseCase,
    private val enrichQuotes: EnrichQuotesUseCase,
) {
    operator fun invoke(filter: QuoteFilter = QuoteFilter()): Flow<List<EnrichedQuote>> =
        enrichQuotes(observeFilteredQuotes(filter))
}
