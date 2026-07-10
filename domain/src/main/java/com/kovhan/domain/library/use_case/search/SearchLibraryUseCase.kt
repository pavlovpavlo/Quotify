package com.kovhan.domain.library.use_case.search

import com.kovhan.core.models.EnrichedQuote
import com.kovhan.core.models.LibrarySearchResults
import com.kovhan.core.models.SavedAuthor
import com.kovhan.core.models.SavedBook
import com.kovhan.core.models.SavedCollection
import com.kovhan.core.models.SavedTag
import javax.inject.Inject

class SearchLibraryUseCase @Inject constructor() {
    operator fun invoke(
        query: String,
        quotes: List<EnrichedQuote>,
        folders: List<SavedCollection>,
        books: List<SavedBook>,
        authors: List<SavedAuthor>,
        tags: List<SavedTag>,
    ): LibrarySearchResults {
        val trimmed = query.trim()
        return LibrarySearchResults(
            quotes = filterQuotes(quotes, trimmed),
            folders = folders.filterByName(trimmed) { it.name },
            books = books.filterByName(trimmed) { it.name },
            authors = authors.filterByName(trimmed) { it.name },
            tags = tags.filterByName(trimmed) { it.name },
        )
    }

    private fun filterQuotes(
        quotes: List<EnrichedQuote>,
        query: String,
    ): List<EnrichedQuote> {
        if (query.isEmpty()) return quotes
        return quotes.filter { quote ->
            quote.text.contains(query, ignoreCase = true) ||
                quote.author?.name?.contains(query, ignoreCase = true) == true ||
                quote.book?.name?.contains(query, ignoreCase = true) == true ||
                quote.tags.any { it.name.contains(query, ignoreCase = true) }
        }
    }

    private inline fun <T> List<T>.filterByName(
        query: String,
        crossinline nameSelector: (T) -> String,
    ): List<T> {
        if (query.isEmpty()) return this
        return filter { item -> nameSelector(item).contains(query, ignoreCase = true) }
    }
}
