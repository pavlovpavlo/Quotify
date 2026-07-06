package com.kovhan.domain.library.use_case.quote

import com.kovhan.core.models.EnrichedQuote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveEnrichedQuoteByIdUseCase @Inject constructor(
    private val observeQuotes: ObserveQuotesUseCase,
    private val enrichQuotes: EnrichQuotesUseCase,
) {
    operator fun invoke(id: String): Flow<EnrichedQuote?> =
        enrichQuotes(observeQuotes().map { quotes -> quotes.filter { it.id == id } })
            .map { it.firstOrNull() }
}
