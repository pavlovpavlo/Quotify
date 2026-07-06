package com.kovhan.domain.library.use_case.quote

import com.kovhan.core.models.Quote
import com.kovhan.domain.library.QuoteRepository
import javax.inject.Inject

class EditQuoteUseCase @Inject constructor(
    private val repository: QuoteRepository,
) {
    suspend operator fun invoke(quote: Quote) = repository.edit(quote)
}
