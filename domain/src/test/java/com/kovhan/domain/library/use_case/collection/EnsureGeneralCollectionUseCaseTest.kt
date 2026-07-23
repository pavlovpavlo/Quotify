package com.kovhan.domain.library.use_case.collection

import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.domain.library.CollectionRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("EnsureGeneralCollectionUseCase")
class EnsureGeneralCollectionUseCaseTest {

    private lateinit var collectionRepository: CollectionRepository
    private lateinit var useCase: EnsureGeneralCollectionUseCase

    @BeforeEach
    fun setUp() {
        collectionRepository = mockk(relaxed = true)
        useCase = EnsureGeneralCollectionUseCase(collectionRepository)
    }

    @Test
    @DisplayName("creates the general collection when it is missing")
    fun createsWhenMissing() = runTest {
        coEvery { collectionRepository.getById(SavedCollection.GENERAL_ID) } returns null

        useCase("General")

        coVerify {
            collectionRepository.edit(
                SavedCollection(id = SavedCollection.GENERAL_ID, name = "General"),
            )
        }
    }

    @Test
    @DisplayName("does nothing when the general collection already exists")
    fun noopWhenPresent() = runTest {
        coEvery { collectionRepository.getById(SavedCollection.GENERAL_ID) } returns
            SavedCollection(id = SavedCollection.GENERAL_ID, name = "General")

        useCase("General")

        coVerify(exactly = 0) { collectionRepository.edit(any()) }
    }
}
