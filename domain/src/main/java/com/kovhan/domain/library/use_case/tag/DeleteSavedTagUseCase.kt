package com.kovhan.domain.library.use_case.tag

import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedTagRepository
import javax.inject.Inject

class DeleteSavedTagUseCase @Inject constructor(
    private val repository: SavedTagRepository,
    private val quoteRepository: QuoteRepository,
) {
    suspend operator fun invoke(id: String) {
        repository.deleteById(id)
        quoteRepository.getAll()
            .filter { id in it.tagIds }
            .forEach { quoteRepository.edit(it.copy(tagIds = it.tagIds - id)) }
    }
}
