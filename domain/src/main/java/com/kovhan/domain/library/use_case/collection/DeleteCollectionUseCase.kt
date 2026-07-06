package com.kovhan.domain.library.use_case.collection

import com.kovhan.domain.library.CollectionRepository
import javax.inject.Inject

class DeleteCollectionUseCase @Inject constructor(
    private val repository: CollectionRepository,
) {
    suspend operator fun invoke(id: String) = repository.deleteById(id)
}
