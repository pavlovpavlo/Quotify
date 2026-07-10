package com.kovhan.domain.library.use_case.quote

import app.cash.turbine.test
import com.kovhan.core.models.quote.Quote
import com.kovhan.core.models.collections.SavedAuthor
import com.kovhan.core.models.collections.SavedBook
import com.kovhan.core.models.collections.SavedTag
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedAuthorRepository
import com.kovhan.domain.library.SavedBookRepository
import com.kovhan.domain.library.SavedTagRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("ObserveEnrichedQuoteByIdUseCase")
class ObserveEnrichedQuoteByIdUseCaseTest {

    private lateinit var quoteRepository: QuoteRepository
    private lateinit var authorRepository: SavedAuthorRepository
    private lateinit var bookRepository: SavedBookRepository
    private lateinit var tagRepository: SavedTagRepository
    private lateinit var collectionRepository: com.kovhan.domain.library.CollectionRepository
    private lateinit var useCase: ObserveEnrichedQuoteByIdUseCase

    private val quote = Quote(id = "q1", text = "x", authorId = "a1", bookId = "b1", tagIds = listOf("t1"))
    private val other = Quote(id = "q2", text = "y")

    @BeforeEach
    fun setUp() {
        quoteRepository = mockk()
        authorRepository = mockk()
        bookRepository = mockk()
        tagRepository = mockk()
        collectionRepository = mockk()
        every { quoteRepository.observeAll() } returns flowOf(listOf(quote, other))
        every { authorRepository.observeAll() } returns flowOf(listOf(SavedAuthor("a1", "Camus")))
        every { bookRepository.observeAll() } returns flowOf(listOf(SavedBook("b1", "The Rebel")))
        every { tagRepository.observeAll() } returns flowOf(listOf(SavedTag("t1", "Absurdism")))
        every { collectionRepository.observeAll() } returns flowOf(emptyList())
        useCase = ObserveEnrichedQuoteByIdUseCase(
            ObserveQuotesUseCase(quoteRepository),
            EnrichQuotesUseCase(authorRepository, bookRepository, tagRepository, collectionRepository),
        )
    }

    @Test
    @DisplayName("emits the enriched quote that matches the id")
    fun emitsMatching() = runTest {
        useCase("q1").test {
            val enriched = awaitItem()

            assertEquals("q1", enriched?.id)
            assertEquals(SavedAuthor("a1", "Camus"), enriched?.author)
            assertEquals(SavedBook("b1", "The Rebel"), enriched?.book)
            assertEquals(listOf(SavedTag("t1", "Absurdism")), enriched?.tags)
            awaitComplete()
        }
    }

    @Test
    @DisplayName("emits null when no quote matches the id")
    fun emitsNullWhenMissing() = runTest {
        useCase("missing").test {
            assertNull(awaitItem())
            awaitComplete()
        }
    }
}
