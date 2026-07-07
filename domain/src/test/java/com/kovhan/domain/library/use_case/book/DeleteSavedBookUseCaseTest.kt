package com.kovhan.domain.library.use_case.book

import com.kovhan.core.models.Quote
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedBookRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("DeleteSavedBookUseCase")
class DeleteSavedBookUseCaseTest {

    private lateinit var bookRepository: SavedBookRepository
    private lateinit var quoteRepository: QuoteRepository
    private lateinit var useCase: DeleteSavedBookUseCase

    @BeforeEach
    fun setUp() {
        bookRepository = mockk(relaxed = true)
        quoteRepository = mockk(relaxed = true)
        useCase = DeleteSavedBookUseCase(bookRepository, quoteRepository)
    }

    @Test
    @DisplayName("deletes the book and clears its id from member quotes only")
    fun deletesAndClears() = runTest {
        val member = Quote(id = "q1", text = "x", bookId = "b1")
        coEvery { quoteRepository.getAll() } returns
            listOf(member, Quote(id = "q2", text = "y", bookId = "other"))

        useCase("b1")

        coVerify { bookRepository.deleteById("b1") }
        coVerify(exactly = 1) { quoteRepository.edit(member.copy(bookId = null)) }
        coVerify(exactly = 0) { quoteRepository.edit(match { it.id != "q1" }) }
    }
}
