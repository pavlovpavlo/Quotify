package com.kovhan.domain.library.use_case.collection

import com.kovhan.core.models.SavedCollection
import com.kovhan.domain.library.CollectionRepository
import javax.inject.Inject

class GetCollectionByIdUseCase @Inject constructor(
    private val repository: CollectionRepository,
) {
    suspend operator fun invoke(id: String): SavedCollection? = repository.getById(id)
}
