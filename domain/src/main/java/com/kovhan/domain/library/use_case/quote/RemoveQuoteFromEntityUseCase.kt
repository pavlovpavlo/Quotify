package com.kovhan.domain.library.use_case.quote

import com.kovhan.core.models.LibraryEntityType
import com.kovhan.core.models.SavedCollection
import javax.inject.Inject

class RemoveQuoteFromEntityUseCase @Inject constructor(
    private val getQuoteById: GetQuoteByIdUseCase,
    private val editQuote: EditQuoteUseCase,
    private val deleteQuote: DeleteQuoteUseCase,
) {
    suspend operator fun invoke(
        quoteId: String,
        entityType: LibraryEntityType,
        entityId: String,
        generalName: String,
    ) {
        when (entityType) {
            LibraryEntityType.COLLECTION -> {
                if (entityId == SavedCollection.GENERAL_ID) {
                    deleteQuote(quoteId)
                    return
                }
                val quote = getQuoteById(quoteId) ?: return
                editQuote(quote.copy(collectionId = SavedCollection.GENERAL_ID), generalName)
            }
            LibraryEntityType.TAG -> {
                val quote = getQuoteById(quoteId) ?: return
                editQuote(quote.copy(tagIds = quote.tagIds - entityId), generalName)
            }
            LibraryEntityType.BOOK -> {
                val quote = getQuoteById(quoteId) ?: return
                editQuote(quote.copy(bookId = null), generalName)
            }
            LibraryEntityType.AUTHOR -> {
                val quote = getQuoteById(quoteId) ?: return
                editQuote(quote.copy(authorId = null), generalName)
            }
        }
    }
}
