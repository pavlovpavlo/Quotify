package com.kovhan.domain.library.use_case.collection

import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.domain.library.CollectionRepository
import javax.inject.Inject

class EnsureGeneralCollectionUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository,
) {
    suspend operator fun invoke(generalName: String) {
        if (collectionRepository.getById(SavedCollection.GENERAL_ID) == null) {
            collectionRepository.edit(
                SavedCollection(id = SavedCollection.GENERAL_ID, name = generalName),
            )
        }
    }
}
