package com.kovhan.domain.daily.use_case

import com.kovhan.core.models.quote.DailyQuote
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.domain.library.CollectionRepository
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedAuthorRepository
import com.kovhan.domain.library.SavedBookRepository
import com.kovhan.domain.library.use_case.quote.DeleteQuoteUseCase
import com.kovhan.domain.widget.use_case.content.HandleWidgetQuoteRemovalUseCase
import javax.inject.Inject

class RemoveDailyQuoteFromFavouritesUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository,
    private val authorRepository: SavedAuthorRepository,
    private val bookRepository: SavedBookRepository,
    private val quoteRepository: QuoteRepository,
    private val deleteQuote: DeleteQuoteUseCase,
    private val handleWidgetQuoteRemoval: HandleWidgetQuoteRemovalUseCase,
) {
    suspend operator fun invoke(dailyQuote: DailyQuote) {
        val quote = quoteRepository.getAll().firstOrNull {
            it.isFavourite && it.sourceDailyId == dailyQuote.id
        } ?: return

        if (quote.collectionId != null) {
            quoteRepository.edit(quote.copy(isFavourite = false))
            handleWidgetQuoteRemoval(quote.id)
            cleanUpFavouritesCollection()
            return
        }

        deleteQuote(quote.id)

        val remaining = quoteRepository.getAll()

        quote.authorId?.let { authorId ->
            if (remaining.none { it.authorId == authorId }) authorRepository.deleteById(authorId)
        }
        quote.bookId?.let { bookId ->
            if (remaining.none { it.bookId == bookId }) bookRepository.deleteById(bookId)
        }
        if (remaining.none { it.isFavourite }) {
            collectionRepository.deleteById(SavedCollection.FAVOURITES_ID)
        }
    }

    private suspend fun cleanUpFavouritesCollection() {
        if (quoteRepository.getAll().none { it.isFavourite }) {
            collectionRepository.deleteById(SavedCollection.FAVOURITES_ID)
        }
    }
}
