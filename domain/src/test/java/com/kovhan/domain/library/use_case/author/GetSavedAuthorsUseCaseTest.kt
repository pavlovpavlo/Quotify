package com.kovhan.domain.library.use_case.author

import com.kovhan.core.models.Quote
import com.kovhan.core.models.SavedAuthor
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedAuthorRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("GetSavedAuthorsUseCase")
class GetSavedAuthorsUseCaseTest {

    private lateinit var authorRepository: SavedAuthorRepository
    private lateinit var quoteRepository: QuoteRepository
    private lateinit var useCase: GetSavedAuthorsUseCase

    @BeforeEach
    fun setUp() {
        authorRepository = mockk()
        quoteRepository = mockk()
        useCase = GetSavedAuthorsUseCase(authorRepository, quoteRepository)
    }

    @Test
    @DisplayName("returns authors without touching quotes by default")
    fun withoutCounts() = runTest {
        coEvery { authorRepository.getAll() } returns listOf(SavedAuthor("a1", "Camus"))

        assertEquals(listOf(SavedAuthor("a1", "Camus", quoteCount = null)), useCase())
        coVerify(exactly = 0) { quoteRepository.getAll() }
    }

    @Test
    @DisplayName("populates the quote count per author when requested")
    fun withCounts() = runTest {
        coEvery { authorRepository.getAll() } returns
            listOf(SavedAuthor("a1", "Camus"), SavedAuthor("a2", "Seneca"))
        coEvery { quoteRepository.getAll() } returns
            listOf(
                Quote(id = "q1", text = "x", authorId = "a1"),
                Quote(id = "q2", text = "y", authorId = "a1"),
                Quote(id = "q3", text = "z", authorId = "a2"),
            )

        assertEquals(
            listOf(
                SavedAuthor("a1", "Camus", quoteCount = 2),
                SavedAuthor("a2", "Seneca", quoteCount = 1),
            ),
            useCase(withCount = true),
        )
    }
}
