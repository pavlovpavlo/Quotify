package com.kovhan.domain.library.use_case.quote

import com.kovhan.core.models.quote.Quote
import com.kovhan.core.models.quote.QuoteFilter
import com.kovhan.domain.library.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveQuotesByCollectionUseCase @Inject constructor(
    private val repository: QuoteRepository,
) {
    operator fun invoke(collectionId: String): Flow<List<Quote>> =
        repository.observeFiltered(QuoteFilter(collectionId = collectionId))
}
