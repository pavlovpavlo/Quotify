package com.kovhan.domain.library.use_case.quote

import com.kovhan.core.models.LibraryEntityType
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.models.quote.Quote
import com.kovhan.domain.library.CollectionRepository
import com.kovhan.domain.widget.use_case.content.HandleWidgetQuoteRemovalUseCase
import javax.inject.Inject

class RemoveQuoteFromEntityUseCase @Inject constructor(
    private val getQuoteById: GetQuoteByIdUseCase,
    private val getQuotes: GetQuotesUseCase,
    private val editQuote: EditQuoteUseCase,
    private val deleteQuote: DeleteQuoteUseCase,
    private val collectionRepository: CollectionRepository,
    private val handleWidgetQuoteRemoval: HandleWidgetQuoteRemovalUseCase,
) {
    suspend operator fun invoke(
        quoteId: String,
        entityType: LibraryEntityType,
        entityId: String,
        generalName: String,
    ) {
        when (entityType) {
            LibraryEntityType.COLLECTION -> removeFromCollection(quoteId, entityId, generalName)

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

    private suspend fun removeFromCollection(
        quoteId: String,
        collectionId: String,
        generalName: String,
    ) {
        if (collectionId == SavedCollection.FAVOURITES_ID) {
            removeFromFavourites(quoteId, generalName)
            return
        }

        if (collectionId == SavedCollection.GENERAL_ID) {
            deleteQuote(quoteId)
            return
        }

        val quote = getQuoteById(quoteId) ?: return
        val target = if (quote.isFavourite) null else SavedCollection.GENERAL_ID
        editQuote(quote.copy(collectionId = target), generalName)
    }

    private suspend fun removeFromFavourites(quoteId: String, generalName: String) {
        val quote = getQuoteById(quoteId) ?: return
        if (quote.collectionId == null) {
            deleteQuote(quoteId)
        } else {
            editQuote(quote.copy(isFavourite = false), generalName)
            handleWidgetQuoteRemoval(quoteId)
        }
        dropEmptyFavouritesCollection()
    }

    private suspend fun dropEmptyFavouritesCollection() {
        if (getQuotes().none(Quote::isFavourite)) {
            collectionRepository.deleteById(SavedCollection.FAVOURITES_ID)
        }
    }
}
