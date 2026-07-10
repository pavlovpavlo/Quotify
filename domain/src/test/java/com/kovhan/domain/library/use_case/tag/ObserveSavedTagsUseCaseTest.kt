package com.kovhan.domain.library.use_case.tag

import app.cash.turbine.test
import com.kovhan.core.models.quote.Quote
import com.kovhan.core.models.collections.SavedTag
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedTagRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("ObserveSavedTagsUseCase")
class ObserveSavedTagsUseCaseTest {

    private lateinit var tagRepository: SavedTagRepository
    private lateinit var quoteRepository: QuoteRepository
    private lateinit var useCase: ObserveSavedTagsUseCase

    @BeforeEach
    fun setUp() {
        tagRepository = mockk()
        quoteRepository = mockk()
        useCase = ObserveSavedTagsUseCase(tagRepository, quoteRepository)
    }

    @Test
    @DisplayName("counts every quote containing the tag, across multi-tag quotes, when requested")
    fun withCounts() = runTest {
        every { tagRepository.observeAll() } returns flowOf(
            listOf(
                SavedTag("t1", "Absurdism"),
                SavedTag("t2", "Stoicism"),
                SavedTag("t3", "Unused"),
            ),
        )
        every { quoteRepository.observeAll() } returns flowOf(
            listOf(
                Quote(id = "q1", text = "x", tagIds = listOf("t1", "t2")),
                Quote(id = "q2", text = "y", tagIds = listOf("t1")),
                Quote(id = "q3", text = "z", tagIds = emptyList()),
            ),
        )

        useCase(withCount = true).test {
            assertEquals(
                listOf(
                    SavedTag("t1", "Absurdism", quoteCount = 2),
                    SavedTag("t2", "Stoicism", quoteCount = 1),
                    SavedTag("t3", "Unused", quoteCount = 0),
                ),
                awaitItem(),
            )
            awaitComplete()
        }
    }
}
