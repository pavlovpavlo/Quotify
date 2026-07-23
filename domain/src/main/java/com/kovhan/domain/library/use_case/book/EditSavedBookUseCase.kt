package com.kovhan.domain.library.use_case.book

import com.kovhan.core.models.collections.SavedBook
import com.kovhan.domain.library.SavedBookRepository
import javax.inject.Inject

class EditSavedBookUseCase @Inject constructor(
    private val repository: SavedBookRepository,
) {
    suspend operator fun invoke(item: SavedBook) = repository.edit(item)
}
