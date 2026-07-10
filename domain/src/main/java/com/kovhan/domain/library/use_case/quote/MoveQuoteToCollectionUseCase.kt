package com.kovhan.domain.library.use_case.quote

import javax.inject.Inject

class MoveQuoteToCollectionUseCase @Inject constructor(
    private val getQuoteById: GetQuoteByIdUseCase,
    private val editQuote: EditQuoteUseCase,
) {
    suspend operator fun invoke(
        quoteId: String,
        targetCollectionId: String,
        generalName: String,
    ) {
        val quote = getQuoteById(quoteId) ?: return
        editQuote(quote.copy(collectionId = targetCollectionId), generalName)
    }
}
