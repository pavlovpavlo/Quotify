package com.kovhan.domain.library.use_case.book

import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedBookRepository
import javax.inject.Inject

class DeleteSavedBookUseCase @Inject constructor(
    private val repository: SavedBookRepository,
    private val quoteRepository: QuoteRepository,
) {
    suspend operator fun invoke(id: String) {
        repository.deleteById(id)
        quoteRepository.getAll()
            .filter { it.bookId == id }
            .forEach { quoteRepository.edit(it.copy(bookId = null)) }
    }
}
