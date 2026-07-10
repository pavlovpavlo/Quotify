package com.kovhan.domain.library.use_case.author

import app.cash.turbine.test
import com.kovhan.core.models.quote.Quote
import com.kovhan.core.models.collections.SavedAuthor
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedAuthorRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("ObserveSavedAuthorsUseCase")
class ObserveSavedAuthorsUseCaseTest {

    private lateinit var authorRepository: SavedAuthorRepository
    private lateinit var quoteRepository: QuoteRepository
    private lateinit var useCase: ObserveSavedAuthorsUseCase

    @BeforeEach
    fun setUp() {
        authorRepository = mockk()
        quoteRepository = mockk()
        useCase = ObserveSavedAuthorsUseCase(authorRepository, quoteRepository)
    }

    @Test
    @DisplayName("returns authors with a null count by default")
    fun withoutCounts() = runTest {
        every { authorRepository.observeAll() } returns flowOf(listOf(SavedAuthor("a1", "Camus")))

        useCase().test {
            assertEquals(listOf(SavedAuthor("a1", "Camus", quoteCount = null)), awaitItem())
            awaitComplete()
        }
    }

    @Test
    @DisplayName("populates the quote count per author when requested")
    fun withCounts() = runTest {
        every { authorRepository.observeAll() } returns flowOf(
            listOf(
                SavedAuthor("a1", "Camus"),
                SavedAuthor("a2", "Seneca"),
                SavedAuthor("a3", "Nobody"),
            ),
        )
        every { quoteRepository.observeAll() } returns flowOf(
            listOf(
                Quote(id = "q1", text = "x", authorId = "a1"),
                Quote(id = "q2", text = "y", authorId = "a1"),
                Quote(id = "q3", text = "z", authorId = "a2"),
                Quote(id = "q4", text = "w", authorId = null),
            ),
        )

        useCase(withCount = true).test {
            assertEquals(
                listOf(
                    SavedAuthor("a1", "Camus", quoteCount = 2),
                    SavedAuthor("a2", "Seneca", quoteCount = 1),
                    SavedAuthor("a3", "Nobody", quoteCount = 0),
                ),
                awaitItem(),
            )
            awaitComplete()
        }
    }
}
