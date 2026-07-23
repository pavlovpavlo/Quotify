package com.kovhan.domain.library.use_case.book

import com.kovhan.core.models.quote.QuoteFilter
import com.kovhan.core.models.collections.SavedBook
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedBookRepository
import com.kovhan.domain.library.matches
import javax.inject.Inject

class GetSavedBooksUseCase @Inject constructor(
    private val bookRepository: SavedBookRepository,
    private val quoteRepository: QuoteRepository,
) {
    suspend operator fun invoke(withCount: Boolean = false): List<SavedBook> {
        val books = bookRepository.getAll()
        if (!withCount) return books
        val quotes = quoteRepository.getAll()
        return books.map { book ->
            book.copy(quoteCount = quotes.count { it.matches(QuoteFilter(bookId = book.id)) })
        }
    }
}
