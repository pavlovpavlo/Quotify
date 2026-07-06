package com.kovhan.domain.library.use_case.author

import com.kovhan.domain.library.SavedAuthorRepository
import javax.inject.Inject

class DeleteSavedAuthorUseCase @Inject constructor(
    private val repository: SavedAuthorRepository,
) {
    suspend operator fun invoke(id: String) = repository.deleteById(id)
}
