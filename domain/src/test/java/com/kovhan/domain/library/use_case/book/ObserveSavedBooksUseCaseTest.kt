package com.kovhan.domain.library.use_case.book

import app.cash.turbine.test
import com.kovhan.core.models.quote.Quote
import com.kovhan.core.models.collections.SavedBook
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedBookRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("ObserveSavedBooksUseCase")
class ObserveSavedBooksUseCaseTest {

    private lateinit var bookRepository: SavedBookRepository
    private lateinit var quoteRepository: QuoteRepository
    private lateinit var useCase: ObserveSavedBooksUseCase

    @BeforeEach
    fun setUp() {
        bookRepository = mockk()
        quoteRepository = mockk()
        useCase = ObserveSavedBooksUseCase(bookRepository, quoteRepository)
    }

    @Test
    @DisplayName("populates the quote count per book when requested")
    fun withCounts() = runTest {
        every { bookRepository.observeAll() } returns flowOf(
            listOf(SavedBook("b1", "The Stranger"), SavedBook("b2", "Letters")),
        )
        every { quoteRepository.observeAll() } returns flowOf(
            listOf(
                Quote(id = "q1", text = "x", bookId = "b1"),
                Quote(id = "q2", text = "y", bookId = null),
            ),
        )

        useCase(withCount = true).test {
            assertEquals(
                listOf(
                    SavedBook("b1", "The Stranger", quoteCount = 1),
                    SavedBook("b2", "Letters", quoteCount = 0),
                ),
                awaitItem(),
            )
            awaitComplete()
        }
    }
}
