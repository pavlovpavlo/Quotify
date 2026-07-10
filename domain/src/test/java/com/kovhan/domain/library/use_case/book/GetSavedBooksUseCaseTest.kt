package com.kovhan.domain.library.use_case.book

import com.kovhan.core.models.quote.Quote
import com.kovhan.core.models.collections.SavedBook
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedBookRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("GetSavedBooksUseCase")
class GetSavedBooksUseCaseTest {

    private lateinit var bookRepository: SavedBookRepository
    private lateinit var quoteRepository: QuoteRepository
    private lateinit var useCase: GetSavedBooksUseCase

    @BeforeEach
    fun setUp() {
        bookRepository = mockk()
        quoteRepository = mockk()
        useCase = GetSavedBooksUseCase(bookRepository, quoteRepository)
    }

    @Test
    @DisplayName("returns books without touching quotes by default")
    fun withoutCounts() = runTest {
        coEvery { bookRepository.getAll() } returns listOf(SavedBook("b1", "The Stranger"))

        assertEquals(listOf(SavedBook("b1", "The Stranger", quoteCount = null)), useCase())
        coVerify(exactly = 0) { quoteRepository.getAll() }
    }

    @Test
    @DisplayName("populates the quote count per book when requested")
    fun withCounts() = runTest {
        coEvery { bookRepository.getAll() } returns
            listOf(SavedBook("b1", "The Stranger"), SavedBook("b2", "Letters"))
        coEvery { quoteRepository.getAll() } returns
            listOf(
                Quote(id = "q1", text = "x", bookId = "b1"),
                Quote(id = "q2", text = "y", bookId = "b1"),
                Quote(id = "q3", text = "z", bookId = "b2"),
            )

        assertEquals(
            listOf(
                SavedBook("b1", "The Stranger", quoteCount = 2),
                SavedBook("b2", "Letters", quoteCount = 1),
            ),
            useCase(withCount = true),
        )
    }
}
