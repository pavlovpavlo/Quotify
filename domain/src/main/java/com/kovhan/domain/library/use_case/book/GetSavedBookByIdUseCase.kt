package com.kovhan.domain.library.use_case.book

import com.kovhan.core.models.SavedBook
import com.kovhan.domain.library.SavedBookRepository
import javax.inject.Inject

class GetSavedBookByIdUseCase @Inject constructor(
    private val repository: SavedBookRepository,
) {
    suspend operator fun invoke(id: String): SavedBook? = repository.getById(id)
}
