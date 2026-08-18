package com.kovhan.domain.daily.use_case

import com.kovhan.core.models.quote.DailyQuote
import com.kovhan.core.models.quote.Quote
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.domain.library.CollectionRepository
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedAuthorRepository
import com.kovhan.domain.library.SavedBookRepository
import com.kovhan.domain.library.use_case.quote.DeleteQuoteUseCase
import com.kovhan.domain.widget.use_case.content.HandleWidgetQuoteRemovalUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("RemoveDailyQuoteFromFavouritesUseCase")
class RemoveDailyQuoteFromFavouritesUseCaseTest {

    private lateinit var collectionRepository: CollectionRepository
    private lateinit var authorRepository: SavedAuthorRepository
    private lateinit var bookRepository: SavedBookRepository
    private lateinit var quoteRepository: QuoteRepository
    private lateinit var deleteQuote: DeleteQuoteUseCase
    private lateinit var handleWidgetQuoteRemoval: HandleWidgetQuoteRemovalUseCase
    private lateinit var useCase: RemoveDailyQuoteFromFavouritesUseCase

    private val daily = DailyQuote(id = "d1", textEn = "en", textUk = "uk")

    private val fav = Quote(
        id = "q1",
        text = "en",
        authorId = "a1",
        bookId = "b1",
        isFavourite = true,
        sourceDailyId = "d1",
    )

    @BeforeEach
    fun setUp() {
        collectionRepository = mockk(relaxed = true)
        authorRepository = mockk(relaxed = true)
        bookRepository = mockk(relaxed = true)
        quoteRepository = mockk(relaxed = true)
        deleteQuote = mockk(relaxed = true)
        handleWidgetQuoteRemoval = mockk(relaxed = true)
        useCase = RemoveDailyQuoteFromFavouritesUseCase(
            collectionRepository,
            authorRepository,
            bookRepository,
            quoteRepository,
            deleteQuote,
            handleWidgetQuoteRemoval,
        )
    }

    @Test
    @DisplayName("removes the linked quote, its orphan author/book and the empty favourites collection")
    fun removesEverythingWhenOrphaned() = runTest {
        coEvery { quoteRepository.getAll() } returns listOf(fav) andThen emptyList()

        useCase(daily)

        coVerify { deleteQuote("q1") }
        coVerify { authorRepository.deleteById("a1") }
        coVerify { bookRepository.deleteById("b1") }
        coVerify { collectionRepository.deleteById(SavedCollection.FAVOURITES_ID) }
    }

    @Test
    @DisplayName("keeps the author, book and collection when still used by another quote")
    fun keepsSharedReferences() = runTest {
        val other = Quote(
            id = "q2",
            text = "x",
            authorId = "a1",
            bookId = "b1",
            isFavourite = true,
        )
        coEvery { quoteRepository.getAll() } returns listOf(fav, other) andThen listOf(other)

        useCase(daily)

        coVerify { deleteQuote("q1") }
        coVerify(exactly = 0) { authorRepository.deleteById(any()) }
        coVerify(exactly = 0) { bookRepository.deleteById(any()) }
        coVerify(exactly = 0) { collectionRepository.deleteById(any()) }
    }

    @Test
    @DisplayName("only clears the flag when the quote also lives in a folder")
    fun keepsQuoteThatWasMovedToFolder() = runTest {
        val moved = fav.copy(collectionId = "c1")
        coEvery { quoteRepository.getAll() } returns listOf(moved) andThen listOf(moved)
        val saved = slot<Quote>()

        useCase(daily)

        coVerify(exactly = 0) { deleteQuote(any()) }
        coVerify { quoteRepository.edit(capture(saved)) }
        coVerify { handleWidgetQuoteRemoval("q1") }
        assertFalse(saved.captured.isFavourite)
        assertEquals("c1", saved.captured.collectionId)
    }

    @Test
    @DisplayName("does nothing when no favourite matches the daily quote")
    fun noopWhenNoMatch() = runTest {
        coEvery { quoteRepository.getAll() } returns
            listOf(Quote(id = "q9", text = "z", isFavourite = true))

        useCase(daily)

        coVerify(exactly = 0) { deleteQuote(any()) }
        coVerify(exactly = 0) { collectionRepository.deleteById(any()) }
    }
}
