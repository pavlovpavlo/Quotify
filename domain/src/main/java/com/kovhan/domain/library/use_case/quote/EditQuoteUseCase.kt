package com.kovhan.domain.library.use_case.quote

import com.kovhan.core.models.Quote
import com.kovhan.core.models.SavedCollection
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.use_case.collection.EnsureGeneralCollectionUseCase
import javax.inject.Inject

class EditQuoteUseCase @Inject constructor(
    private val repository: QuoteRepository,
    private val ensureGeneralCollection: EnsureGeneralCollectionUseCase,
) {
    suspend operator fun invoke(quote: Quote, generalName: String) {
        val resolved = if (quote.collectionId.isNullOrBlank()) {
            ensureGeneralCollection(generalName)
            quote.copy(collectionId = SavedCollection.GENERAL_ID)
        } else {
            quote
        }
        repository.edit(resolved)
    }
}
