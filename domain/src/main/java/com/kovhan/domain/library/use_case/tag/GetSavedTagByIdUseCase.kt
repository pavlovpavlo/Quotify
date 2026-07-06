package com.kovhan.domain.library.use_case.tag

import com.kovhan.core.models.SavedTag
import com.kovhan.domain.library.SavedTagRepository
import javax.inject.Inject

class GetSavedTagByIdUseCase @Inject constructor(
    private val repository: SavedTagRepository,
) {
    suspend operator fun invoke(id: String): SavedTag? = repository.getById(id)
}
