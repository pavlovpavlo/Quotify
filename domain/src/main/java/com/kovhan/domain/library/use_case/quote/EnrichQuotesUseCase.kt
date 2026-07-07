package com.kovhan.domain.library.use_case.quote

import com.kovhan.core.models.EnrichedQuote
import com.kovhan.core.models.Quote
import com.kovhan.domain.library.CollectionRepository
import com.kovhan.domain.library.SavedAuthorRepository
import com.kovhan.domain.library.SavedBookRepository
import com.kovhan.domain.library.SavedTagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class EnrichQuotesUseCase @Inject constructor(
    private val authorRepository: SavedAuthorRepository,
    private val bookRepository: SavedBookRepository,
    private val tagRepository: SavedTagRepository,
    private val collectionRepository: CollectionRepository,
) {
    operator fun invoke(quotes: Flow<List<Quote>>): Flow<List<EnrichedQuote>> =
        combine(
            quotes,
            authorRepository.observeAll(),
            bookRepository.observeAll(),
            tagRepository.observeAll(),
            collectionRepository.observeAll(),
        ) { quoteList, authors, books, tags, collections ->
            val authorsById = authors.associateBy { it.id }
            val booksById = books.associateBy { it.id }
            val tagsById = tags.associateBy { it.id }
            val collectionsById = collections.associateBy { it.id }
            quoteList.map { quote ->
                EnrichedQuote(
                    id = quote.id,
                    text = quote.text,
                    author = quote.authorId?.let { authorsById[it] },
                    book = quote.bookId?.let { booksById[it] },
                    collection = quote.collectionId?.let { collectionsById[it] },
                    tags = quote.tagIds.mapNotNull { tagsById[it] },
                    inPushPlaylist = quote.inPushPlaylist,
                    inWidgetPlaylist = quote.inWidgetPlaylist,
                )
            }
        }
}
