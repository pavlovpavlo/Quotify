package com.kovhan.domain.library.use_case.quote

import com.kovhan.core.models.quote.Quote
import com.kovhan.domain.library.QuoteRepository
import javax.inject.Inject

class GetQuotesUseCase @Inject constructor(
    private val repository: QuoteRepository,
) {
    suspend operator fun invoke(): List<Quote> = repository.getAll()
}
