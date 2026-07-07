package com.kovhan.domain.library.use_case.collection

import com.kovhan.core.models.Quote
import com.kovhan.core.models.SavedCollection
import com.kovhan.domain.library.CollectionRepository
import com.kovhan.domain.library.QuoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("DeleteCollectionUseCase")
class DeleteCollectionUseCaseTest {

    private lateinit var collectionRepository: CollectionRepository
    private lateinit var quoteRepository: QuoteRepository
    private lateinit var ensureGeneralCollection: EnsureGeneralCollectionUseCase
    private lateinit var useCase: DeleteCollectionUseCase

    @BeforeEach
    fun setUp() {
        collectionRepository = mockk(relaxed = true)
        quoteRepository = mockk(relaxed = true)
        ensureGeneralCollection = mockk(relaxed = true)
        useCase = DeleteCollectionUseCase(collectionRepository, quoteRepository, ensureGeneralCollection)
    }

    @Test
    @DisplayName("deletes the collection and re-homes member quotes into general")
    fun deletesAndReHomes() = runTest {
        val member = Quote(id = "q1", text = "x", collectionId = "c1")
        coEvery { quoteRepository.getAll() } returns
            listOf(
                member,
                Quote(id = "q2", text = "y", collectionId = "other"),
                Quote(id = "q3", text = "z", collectionId = null),
            )

        useCase("c1", "General")

        coVerify { ensureGeneralCollection("General") }
        coVerify { collectionRepository.deleteById("c1") }
        coVerify(exactly = 1) {
            quoteRepository.edit(member.copy(collectionId = SavedCollection.GENERAL_ID))
        }
        coVerify(exactly = 0) { quoteRepository.edit(match { it.id != "q1" }) }
    }

    @Test
    @DisplayName("is a no-op when deleting the general collection")
    fun ignoresGeneral() = runTest {
        useCase(SavedCollection.GENERAL_ID, "General")

        coVerify(exactly = 0) { collectionRepository.deleteById(any()) }
        coVerify(exactly = 0) { ensureGeneralCollection(any()) }
    }

    @Test
    @DisplayName("is a no-op when deleting the favourites collection")
    fun ignoresFavourites() = runTest {
        useCase(SavedCollection.FAVOURITES_ID, "General")

        coVerify(exactly = 0) { collectionRepository.deleteById(any()) }
        coVerify(exactly = 0) { ensureGeneralCollection(any()) }
    }
}
