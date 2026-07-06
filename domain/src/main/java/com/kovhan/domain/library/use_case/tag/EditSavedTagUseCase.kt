package com.kovhan.domain.library.use_case.tag

import com.kovhan.core.models.SavedTag
import com.kovhan.domain.library.SavedTagRepository
import javax.inject.Inject

class EditSavedTagUseCase @Inject constructor(
    private val repository: SavedTagRepository,
) {
    suspend operator fun invoke(item: SavedTag) = repository.edit(item)
}
