package com.kovhan.domain.library.use_case.collection

import com.kovhan.core.models.SavedCollection
import com.kovhan.domain.library.CollectionRepository
import com.kovhan.domain.library.QuoteRepository
import javax.inject.Inject

class DeleteCollectionUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository,
    private val quoteRepository: QuoteRepository,
    private val ensureGeneralCollection: EnsureGeneralCollectionUseCase,
) {
    suspend operator fun invoke(id: String, generalName: String) {
        if (id == SavedCollection.GENERAL_ID || id == SavedCollection.FAVOURITES_ID) return

        ensureGeneralCollection(generalName)
        collectionRepository.deleteById(id)
        quoteRepository.getAll()
            .filter { it.collectionId == id }
            .forEach { quoteRepository.edit(it.copy(collectionId = SavedCollection.GENERAL_ID)) }
    }
}
