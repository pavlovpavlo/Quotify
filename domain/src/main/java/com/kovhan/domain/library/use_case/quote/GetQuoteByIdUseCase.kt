package com.kovhan.domain.library.use_case.quote

import com.kovhan.core.models.quote.Quote
import com.kovhan.domain.library.QuoteRepository
import javax.inject.Inject

class GetQuoteByIdUseCase @Inject constructor(
    private val repository: QuoteRepository,
) {
    suspend operator fun invoke(id: String): Quote? = repository.getById(id)
}
