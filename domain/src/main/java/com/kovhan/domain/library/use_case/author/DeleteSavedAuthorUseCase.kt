package com.kovhan.domain.library.use_case.author

import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedAuthorRepository
import javax.inject.Inject

class DeleteSavedAuthorUseCase @Inject constructor(
    private val repository: SavedAuthorRepository,
    private val quoteRepository: QuoteRepository,
) {
    suspend operator fun invoke(id: String) {
        repository.deleteById(id)
        quoteRepository.getAll()
            .filter { it.authorId == id }
            .forEach { quoteRepository.edit(it.copy(authorId = null)) }
    }
}
