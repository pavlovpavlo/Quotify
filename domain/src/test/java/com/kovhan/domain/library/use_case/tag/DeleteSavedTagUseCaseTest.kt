package com.kovhan.domain.library.use_case.tag

import com.kovhan.core.models.Quote
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedTagRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("DeleteSavedTagUseCase")
class DeleteSavedTagUseCaseTest {

    private lateinit var tagRepository: SavedTagRepository
    private lateinit var quoteRepository: QuoteRepository
    private lateinit var useCase: DeleteSavedTagUseCase

    @BeforeEach
    fun setUp() {
        tagRepository = mockk(relaxed = true)
        quoteRepository = mockk(relaxed = true)
        useCase = DeleteSavedTagUseCase(tagRepository, quoteRepository)
    }

    @Test
    @DisplayName("deletes the tag and removes its id from the tag list of member quotes only")
    fun deletesAndRemovesFromList() = runTest {
        val member = Quote(id = "q1", text = "x", tagIds = listOf("t1", "t2"))
        coEvery { quoteRepository.getAll() } returns
            listOf(member, Quote(id = "q2", text = "y", tagIds = listOf("t2")))

        useCase("t1")

        coVerify { tagRepository.deleteById("t1") }
        coVerify(exactly = 1) { quoteRepository.edit(member.copy(tagIds = listOf("t2"))) }
        coVerify(exactly = 0) { quoteRepository.edit(match { it.id != "q1" }) }
    }
}
