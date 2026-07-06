package com.kovhan.domain.library.use_case.tag

import com.kovhan.domain.library.SavedTagRepository
import javax.inject.Inject

class DeleteSavedTagUseCase @Inject constructor(
    private val repository: SavedTagRepository,
) {
    suspend operator fun invoke(id: String) = repository.deleteById(id)
}
