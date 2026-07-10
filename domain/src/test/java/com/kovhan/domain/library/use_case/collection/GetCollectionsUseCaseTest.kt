package com.kovhan.domain.library.use_case.collection

import com.kovhan.core.models.quote.Quote
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.domain.library.CollectionRepository
import com.kovhan.domain.library.QuoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("GetCollectionsUseCase")
class GetCollectionsUseCaseTest {

    private lateinit var collectionRepository: CollectionRepository
    private lateinit var quoteRepository: QuoteRepository
    private lateinit var useCase: GetCollectionsUseCase

    @BeforeEach
    fun setUp() {
        collectionRepository = mockk()
        quoteRepository = mockk()
        useCase = GetCollectionsUseCase(collectionRepository, quoteRepository)
    }

    @Test
    @DisplayName("returns collections without touching quotes by default")
    fun withoutCounts() = runTest {
        coEvery { collectionRepository.getAll() } returns listOf(SavedCollection("c1", "Favourites"))

        assertEquals(listOf(SavedCollection("c1", "Favourites", quoteCount = null)), useCase())
        coVerify(exactly = 0) { quoteRepository.getAll() }
    }

    @Test
    @DisplayName("populates the quote count per collection when requested")
    fun withCounts() = runTest {
        coEvery { collectionRepository.getAll() } returns
            listOf(SavedCollection("c1", "Favourites"), SavedCollection("c2", "Stoics"))
        coEvery { quoteRepository.getAll() } returns
            listOf(
                Quote(id = "q1", text = "x", collectionId = "c1"),
                Quote(id = "q2", text = "y", collectionId = "c1"),
                Quote(id = "q3", text = "z", collectionId = "c2"),
                Quote(id = "q4", text = "w", collectionId = null),
            )

        assertEquals(
            listOf(
                SavedCollection("c1", "Favourites", quoteCount = 2),
                SavedCollection("c2", "Stoics", quoteCount = 1),
            ),
            useCase(withCount = true),
        )
    }
}
