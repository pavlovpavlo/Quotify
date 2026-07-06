package com.kovhan.domain.library.use_case.tag

import com.kovhan.core.models.Quote
import com.kovhan.core.models.SavedTag
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedTagRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("GetSavedTagsUseCase")
class GetSavedTagsUseCaseTest {

    private lateinit var tagRepository: SavedTagRepository
    private lateinit var quoteRepository: QuoteRepository
    private lateinit var useCase: GetSavedTagsUseCase

    @BeforeEach
    fun setUp() {
        tagRepository = mockk()
        quoteRepository = mockk()
        useCase = GetSavedTagsUseCase(tagRepository, quoteRepository)
    }

    @Test
    @DisplayName("returns tags without touching quotes by default")
    fun withoutCounts() = runTest {
        coEvery { tagRepository.getAll() } returns listOf(SavedTag("t1", "Absurdism"))

        assertEquals(listOf(SavedTag("t1", "Absurdism", quoteCount = null)), useCase())
        coVerify(exactly = 0) { quoteRepository.getAll() }
    }

    @Test
    @DisplayName("counts every quote containing the tag, across multi-tag quotes, when requested")
    fun withCounts() = runTest {
        coEvery { tagRepository.getAll() } returns
            listOf(SavedTag("t1", "Absurdism"), SavedTag("t2", "Stoicism"))
        coEvery { quoteRepository.getAll() } returns
            listOf(
                Quote(id = "q1", text = "x", tagIds = listOf("t1", "t2")),
                Quote(id = "q2", text = "y", tagIds = listOf("t1")),
            )

        assertEquals(
            listOf(
                SavedTag("t1", "Absurdism", quoteCount = 2),
                SavedTag("t2", "Stoicism", quoteCount = 1),
            ),
            useCase(withCount = true),
        )
    }
}
