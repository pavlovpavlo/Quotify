package com.kovhan.domain.library.use_case.book

import com.kovhan.core.models.QuoteFilter
import com.kovhan.core.models.SavedBook
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedBookRepository
import com.kovhan.domain.library.matches
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveSavedBooksUseCase @Inject constructor(
    private val bookRepository: SavedBookRepository,
    private val quoteRepository: QuoteRepository,
) {
    operator fun invoke(withCount: Boolean = false): Flow<List<SavedBook>> =
        if (!withCount) {
            bookRepository.observeAll()
        } else {
            combine(
                bookRepository.observeAll(),
                quoteRepository.observeAll(),
            ) { books, quotes ->
                books.map { book ->
                    book.copy(quoteCount = quotes.count { it.matches(QuoteFilter(bookId = book.id)) })
                }
            }
        }
}
