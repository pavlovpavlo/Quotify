package com.kovhan.domain.premium.use_case

import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.domain.billing.use_case.IsSubscribedUseCase
import com.kovhan.domain.library.CollectionRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CheckCollectionLimitUseCase")
class CheckCollectionLimitUseCaseTest {

    private val isSubscribed: IsSubscribedUseCase = mockk()
    private val collectionRepository: CollectionRepository = mockk()
    private val useCase = CheckCollectionLimitUseCase(isSubscribed, collectionRepository)

    @Test
    @DisplayName("system collections do not count toward the free limit")
    fun ignoresSystemCollections() = runTest {
        coEvery { isSubscribed() } returns false
        coEvery { collectionRepository.getAll() } returns listOf(
            collection(SavedCollection.FAVOURITES_ID),
            collection(SavedCollection.GENERAL_ID),
            collection("a"),
            collection("b"),
        )

        assertTrue(useCase())
    }

    @Test
    @DisplayName("free plan blocks the fourth user collection")
    fun blocksFourth() = runTest {
        coEvery { isSubscribed() } returns false
        coEvery { collectionRepository.getAll() } returns listOf(
            collection(SavedCollection.GENERAL_ID),
            collection("a"),
            collection("b"),
            collection("c"),
        )

        assertFalse(useCase())
    }

    @Test
    @DisplayName("subscriber is never limited")
    fun subscriberIsUnlimited() = runTest {
        coEvery { isSubscribed() } returns true

        assertTrue(useCase())
    }

    private fun collection(id: String) = SavedCollection(id = id, name = id)
}
