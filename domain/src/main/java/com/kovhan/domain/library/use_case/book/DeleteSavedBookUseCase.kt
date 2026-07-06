package com.kovhan.domain.library.use_case.book

import com.kovhan.domain.library.SavedBookRepository
import javax.inject.Inject

class DeleteSavedBookUseCase @Inject constructor(
    private val repository: SavedBookRepository,
) {
    suspend operator fun invoke(id: String) = repository.deleteById(id)
}
