package com.kovhan.domain.library.use_case.quote

import com.kovhan.core.models.Quote
import com.kovhan.domain.library.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveQuotesUseCase @Inject constructor(
    private val repository: QuoteRepository,
) {
    operator fun invoke(): Flow<List<Quote>> = repository.observeAll()
}
