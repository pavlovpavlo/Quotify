package com.kovhan.domain.library.use_case.quote

import app.cash.turbine.test
import com.kovhan.core.models.Quote
import com.kovhan.core.models.SavedAuthor
import com.kovhan.core.models.SavedBook
import com.kovhan.core.models.SavedTag
import com.kovhan.domain.library.SavedAuthorRepository
import com.kovhan.domain.library.SavedBookRepository
import com.kovhan.domain.library.SavedTagRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("EnrichQuotesUseCase")
class EnrichQuotesUseCaseTest {

    private lateinit var authorRepository: SavedAuthorRepository
    private lateinit var bookRepository: SavedBookRepository
    private lateinit var tagRepository: SavedTagRepository
    private lateinit var useCase: EnrichQuotesUseCase

    private val quote = Quote(
        id = "q1",
        text = "The only way to deal with an unfree world",
        authorId = "a1",
        bookId = "b1",
        tagIds = listOf("t1", "t2"),
        inPushPlaylist = true,
        inWidgetPlaylist = false,
    )

    @BeforeEach
    fun setUp() {
        authorRepository = mockk()
        bookRepository = mockk()
        tagRepository = mockk()
        useCase = EnrichQuotesUseCase(authorRepository, bookRepository, tagRepository)
    }

    @Test
    @DisplayName("resolves author, book and tags into their current values")
    fun resolvesReferences() = runTest {
        every { authorRepository.observeAll() } returns flowOf(listOf(SavedAuthor("a1", "Camus")))
        every { bookRepository.observeAll() } returns flowOf(listOf(SavedBook("b1", "The Rebel")))
        every { tagRepository.observeAll() } returns
            flowOf(listOf(SavedTag("t1", "Absurdism"), SavedTag("t2", "Rebellion")))

        useCase(flowOf(listOf(quote))).test {
            val enriched = awaitItem().single()

            assertEquals(SavedAuthor("a1", "Camus"), enriched.author)
            assertEquals(SavedBook("b1", "The Rebel"), enriched.book)
            assertEquals(
                listOf(SavedTag("t1", "Absurdism"), SavedTag("t2", "Rebellion")),
                enriched.tags,
            )
            assertTrue(enriched.inPushPlaylist)
            awaitComplete()
        }
    }

    @Test
    @DisplayName("drops references that no longer exist")
    fun dropsDeletedReferences() = runTest {
        every { authorRepository.observeAll() } returns flowOf(emptyList())
        every { bookRepository.observeAll() } returns flowOf(emptyList())
        every { tagRepository.observeAll() } returns flowOf(listOf(SavedTag("t1", "Absurdism")))

        useCase(flowOf(listOf(quote))).test {
            val enriched = awaitItem().single()

            assertNull(enriched.author)
            assertNull(enriched.book)
            assertEquals(listOf(SavedTag("t1", "Absurdism")), enriched.tags)
            awaitComplete()
        }
    }
}
