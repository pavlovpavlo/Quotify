package com.kovhan.domain.library.use_case.quote

import app.cash.turbine.test
import com.kovhan.core.models.Quote
import com.kovhan.core.models.QuoteFilter
import com.kovhan.core.models.SavedAuthor
import com.kovhan.core.models.SavedBook
import com.kovhan.core.models.SavedTag
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedAuthorRepository
import com.kovhan.domain.library.SavedBookRepository
import com.kovhan.domain.library.SavedTagRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("ObserveEnrichedQuotesUseCase")
class ObserveEnrichedQuotesUseCaseTest {

    private lateinit var quoteRepository: QuoteRepository
    private lateinit var authorRepository: SavedAuthorRepository
    private lateinit var bookRepository: SavedBookRepository
    private lateinit var tagRepository: SavedTagRepository
    private lateinit var collectionRepository: com.kovhan.domain.library.CollectionRepository
    private lateinit var useCase: ObserveEnrichedQuotesUseCase

    private val quote = Quote(id = "q1", text = "x", authorId = "a1", bookId = "b1", tagIds = listOf("t1"))

    @BeforeEach
    fun setUp() {
        quoteRepository = mockk()
        authorRepository = mockk()
        bookRepository = mockk()
        tagRepository = mockk()
        collectionRepository = mockk()
        every { authorRepository.observeAll() } returns flowOf(listOf(SavedAuthor("a1", "Camus")))
        every { bookRepository.observeAll() } returns flowOf(listOf(SavedBook("b1", "The Rebel")))
        every { tagRepository.observeAll() } returns flowOf(listOf(SavedTag("t1", "Absurdism")))
        every { collectionRepository.observeAll() } returns flowOf(emptyList())
        useCase = ObserveEnrichedQuotesUseCase(
            ObserveFilteredQuotesUseCase(quoteRepository),
            EnrichQuotesUseCase(authorRepository, bookRepository, tagRepository, collectionRepository),
        )
    }

    @Test
    @DisplayName("enriches the whole list for the default empty filter")
    fun enrichesWholeList() = runTest {
        every { quoteRepository.observeFiltered(QuoteFilter()) } returns flowOf(listOf(quote))

        useCase().test {
            val enriched = awaitItem().single()

            assertEquals(SavedAuthor("a1", "Camus"), enriched.author)
            assertEquals(SavedBook("b1", "The Rebel"), enriched.book)
            assertEquals(listOf(SavedTag("t1", "Absurdism")), enriched.tags)
            awaitComplete()
        }
    }

    @Test
    @DisplayName("passes the filter through before enriching")
    fun passesFilterThrough() = runTest {
        val filter = QuoteFilter(authorId = "a1")
        every { quoteRepository.observeFiltered(filter) } returns flowOf(listOf(quote))

        useCase(filter).test {
            assertEquals("q1", awaitItem().single().id)
            awaitComplete()
        }
    }
}
