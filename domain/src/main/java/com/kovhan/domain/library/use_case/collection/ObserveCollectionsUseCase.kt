package com.kovhan.domain.library.use_case.collection

import com.kovhan.core.models.SavedCollection
import com.kovhan.domain.library.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCollectionsUseCase @Inject constructor(
    private val repository: CollectionRepository,
) {
    operator fun invoke(): Flow<List<SavedCollection>> = repository.observeAll()
}
