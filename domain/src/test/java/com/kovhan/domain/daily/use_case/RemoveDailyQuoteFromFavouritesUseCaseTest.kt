package com.kovhan.domain.daily.use_case

import com.kovhan.core.models.DailyQuote
import com.kovhan.core.models.Quote
import com.kovhan.core.models.SavedCollection
import com.kovhan.domain.library.CollectionRepository
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedAuthorRepository
import com.kovhan.domain.library.SavedBookRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("RemoveDailyQuoteFromFavouritesUseCase")
class RemoveDailyQuoteFromFavouritesUseCaseTest {

    private lateinit var collectionRepository: CollectionRepository
    private lateinit var authorRepository: SavedAuthorRepository
    private lateinit var bookRepository: SavedBookRepository
    private lateinit var quoteRepository: QuoteRepository
    private lateinit var useCase: RemoveDailyQuoteFromFavouritesUseCase

    private val daily = DailyQuote(id = "d1", textEn = "en", textUk = "uk")

    private val fav = Quote(
        id = "q1",
        text = "en",
        authorId = "a1",
        bookId = "b1",
        collectionId = SavedCollection.FAVOURITES_ID,
        sourceDailyId = "d1",
    )

    @BeforeEach
    fun setUp() {
        collectionRepository = mockk(relaxed = true)
        authorRepository = mockk(relaxed = true)
        bookRepository = mockk(relaxed = true)
        quoteRepository = mockk(relaxed = true)
        useCase = RemoveDailyQuoteFromFavouritesUseCase(
            collectionRepository, authorRepository, bookRepository, quoteRepository,
        )
    }

    @Test
    @DisplayName("removes the linked quote, its orphan author/book and the empty favourites collection")
    fun removesEverythingWhenOrphaned() = runTest {
        coEvery { quoteRepository.getAll() } returns listOf(fav) andThen emptyList()

        useCase(daily)

        coVerify { quoteRepository.deleteById("q1") }
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
            collectionId = SavedCollection.FAVOURITES_ID,
        )
        coEvery { quoteRepository.getAll() } returns listOf(fav, other) andThen listOf(other)

        useCase(daily)

        coVerify { quoteRepository.deleteById("q1") }
        coVerify(exactly = 0) { authorRepository.deleteById(any()) }
        coVerify(exactly = 0) { bookRepository.deleteById(any()) }
        coVerify(exactly = 0) { collectionRepository.deleteById(any()) }
    }

    @Test
    @DisplayName("does nothing when no favourite matches the daily quote")
    fun noopWhenNoMatch() = runTest {
        coEvery { quoteRepository.getAll() } returns
            listOf(Quote(id = "q9", text = "z", collectionId = SavedCollection.FAVOURITES_ID))

        useCase(daily)

        coVerify(exactly = 0) { quoteRepository.deleteById(any()) }
        coVerify(exactly = 0) { collectionRepository.deleteById(any()) }
    }
}
