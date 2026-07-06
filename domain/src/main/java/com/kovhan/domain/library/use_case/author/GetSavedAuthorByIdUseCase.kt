package com.kovhan.domain.library.use_case.author

import com.kovhan.core.models.SavedAuthor
import com.kovhan.domain.library.SavedAuthorRepository
import javax.inject.Inject

class GetSavedAuthorByIdUseCase @Inject constructor(
    private val repository: SavedAuthorRepository,
) {
    suspend operator fun invoke(id: String): SavedAuthor? = repository.getById(id)
}
