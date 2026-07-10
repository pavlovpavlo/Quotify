package com.kovhan.domain.library.use_case.collection

import com.kovhan.core.models.quote.QuoteFilter
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.domain.library.CollectionRepository
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.matches
import javax.inject.Inject

class GetCollectionsUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository,
    private val quoteRepository: QuoteRepository,
) {
    suspend operator fun invoke(withCount: Boolean = false): List<SavedCollection> {
        val collections = collectionRepository.getAll()
        if (!withCount) return collections
        val quotes = quoteRepository.getAll()
        return collections
            .map { collection ->
                collection.copy(
                    quoteCount = quotes.count {
                        it.matches(QuoteFilter(collectionId = collection.id))
                    },
                )
            }
            .filterNot { it.id == SavedCollection.GENERAL_ID && it.quoteCount == 0 }
    }
}
