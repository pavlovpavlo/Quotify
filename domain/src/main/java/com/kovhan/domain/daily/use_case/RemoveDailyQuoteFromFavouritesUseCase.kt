package com.kovhan.domain.daily.use_case

import com.kovhan.core.models.quote.DailyQuote
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.domain.library.CollectionRepository
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedAuthorRepository
import com.kovhan.domain.library.SavedBookRepository
import javax.inject.Inject

class RemoveDailyQuoteFromFavouritesUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository,
    private val authorRepository: SavedAuthorRepository,
    private val bookRepository: SavedBookRepository,
    private val quoteRepository: QuoteRepository,
) {
    suspend operator fun invoke(dailyQuote: DailyQuote) {
        val quote = quoteRepository.getAll().firstOrNull {
            it.collectionId == SavedCollection.FAVOURITES_ID && it.sourceDailyId == dailyQuote.id
        } ?: return

        quoteRepository.deleteById(quote.id)

        val remaining = quoteRepository.getAll()

        quote.authorId?.let { authorId ->
            if (remaining.none { it.authorId == authorId }) authorRepository.deleteById(authorId)
        }
        quote.bookId?.let { bookId ->
            if (remaining.none { it.bookId == bookId }) bookRepository.deleteById(bookId)
        }
        if (remaining.none { it.collectionId == SavedCollection.FAVOURITES_ID }) {
            collectionRepository.deleteById(SavedCollection.FAVOURITES_ID)
        }
    }
}
