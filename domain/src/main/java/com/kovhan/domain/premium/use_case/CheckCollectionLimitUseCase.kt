package com.kovhan.domain.premium.use_case

import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.domain.billing.use_case.IsSubscribedUseCase
import com.kovhan.domain.library.CollectionRepository
import com.kovhan.domain.premium.PremiumLimits
import javax.inject.Inject

/**
 * Чи можна створити ще одну колекцію. «Обране» та «Загальна» створює сам
 * застосунок, тому до ліміту користувача вони не входять.
 */
class CheckCollectionLimitUseCase @Inject constructor(
    private val isSubscribed: IsSubscribedUseCase,
    private val collectionRepository: CollectionRepository,
) {
    suspend operator fun invoke(): Boolean {
        if (isSubscribed()) return true
        val userCreated = collectionRepository.getAll().count { it.id !in SYSTEM_COLLECTION_IDS }
        return userCreated < PremiumLimits.FREE_MAX_COLLECTIONS
    }

    private companion object {
        val SYSTEM_COLLECTION_IDS = setOf(
            SavedCollection.FAVOURITES_ID,
            SavedCollection.GENERAL_ID,
        )
    }
}
