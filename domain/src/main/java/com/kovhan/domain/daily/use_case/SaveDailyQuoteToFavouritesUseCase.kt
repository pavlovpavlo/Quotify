package com.kovhan.domain.daily.use_case

import com.kovhan.core.models.DailyQuote
import com.kovhan.core.models.Quote
import com.kovhan.core.models.SavedAuthor
import com.kovhan.core.models.SavedBook
import com.kovhan.core.models.SavedCollection
import com.kovhan.domain.common.IdGenerator
import com.kovhan.domain.daily.localizedAuthor
import com.kovhan.domain.daily.localizedBook
import com.kovhan.domain.daily.localizedText
import com.kovhan.domain.library.CollectionRepository
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedAuthorRepository
import com.kovhan.domain.library.SavedBookRepository
import com.kovhan.domain.settings.AppLanguage
import javax.inject.Inject

class SaveDailyQuoteToFavouritesUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository,
    private val authorRepository: SavedAuthorRepository,
    private val bookRepository: SavedBookRepository,
    private val quoteRepository: QuoteRepository,
    private val idGenerator: IdGenerator,
) {
    suspend operator fun invoke(
        dailyQuote: DailyQuote,
        favouritesName: String,
        language: AppLanguage,
    ) {
        if (collectionRepository.getById(SavedCollection.FAVOURITES_ID) == null) {
            collectionRepository.edit(
                SavedCollection(id = SavedCollection.FAVOURITES_ID, name = favouritesName),
            )
        }

        val authorId = resolveAuthorId(dailyQuote.localizedAuthor(language))
        val bookId = resolveBookId(dailyQuote.localizedBook(language))

        quoteRepository.edit(
            Quote(
                id = idGenerator.generate(),
                text = dailyQuote.localizedText(language),
                authorId = authorId,
                bookId = bookId,
                collectionId = SavedCollection.FAVOURITES_ID,
                sourceDailyId = dailyQuote.id,
            ),
        )
    }

    private suspend fun resolveAuthorId(name: String?): String? {
        val trimmed = name?.trim().takeUnless { it.isNullOrEmpty() } ?: return null
        authorRepository.getAll().firstOrNull { it.name.equals(trimmed, ignoreCase = true) }
            ?.let { return it.id }
        val id = idGenerator.generate()
        authorRepository.edit(SavedAuthor(id = id, name = trimmed))
        return id
    }

    private suspend fun resolveBookId(name: String?): String? {
        val trimmed = name?.trim().takeUnless { it.isNullOrEmpty() } ?: return null
        bookRepository.getAll().firstOrNull { it.name.equals(trimmed, ignoreCase = true) }
            ?.let { return it.id }
        val id = idGenerator.generate()
        bookRepository.edit(SavedBook(id = id, name = trimmed))
        return id
    }
}
