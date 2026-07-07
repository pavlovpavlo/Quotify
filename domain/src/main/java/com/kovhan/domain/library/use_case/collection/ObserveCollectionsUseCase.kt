package com.kovhan.domain.library.use_case.collection

import com.kovhan.core.models.QuoteFilter
import com.kovhan.core.models.SavedCollection
import com.kovhan.domain.library.CollectionRepository
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.matches
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveCollectionsUseCase @Inject constructor(
    private val repository: CollectionRepository,
    private val quoteRepository: QuoteRepository,
) {
    operator fun invoke(): Flow<List<SavedCollection>> =
        combine(repository.observeAll(), quoteRepository.observeAll()) { collections, quotes ->
            collections
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
