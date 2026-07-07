package com.kovhan.domain.library.use_case.author

import com.kovhan.core.models.Quote
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedAuthorRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("DeleteSavedAuthorUseCase")
class DeleteSavedAuthorUseCaseTest {

    private lateinit var authorRepository: SavedAuthorRepository
    private lateinit var quoteRepository: QuoteRepository
    private lateinit var useCase: DeleteSavedAuthorUseCase

    @BeforeEach
    fun setUp() {
        authorRepository = mockk(relaxed = true)
        quoteRepository = mockk(relaxed = true)
        useCase = DeleteSavedAuthorUseCase(authorRepository, quoteRepository)
    }

    @Test
    @DisplayName("deletes the author and clears its id from member quotes only")
    fun deletesAndClears() = runTest {
        val member = Quote(id = "q1", text = "x", authorId = "a1")
        coEvery { quoteRepository.getAll() } returns
            listOf(member, Quote(id = "q2", text = "y", authorId = "other"))

        useCase("a1")

        coVerify { authorRepository.deleteById("a1") }
        coVerify(exactly = 1) { quoteRepository.edit(member.copy(authorId = null)) }
        coVerify(exactly = 0) { quoteRepository.edit(match { it.id != "q1" }) }
    }
}
