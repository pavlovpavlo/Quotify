package com.kovhan.domain.daily.use_case

import com.kovhan.core.models.DailyQuote
import com.kovhan.core.models.Quote
import com.kovhan.core.models.SavedAuthor
import com.kovhan.core.models.SavedBook
import com.kovhan.core.models.SavedCollection
import com.kovhan.domain.common.IdGenerator
import com.kovhan.domain.library.CollectionRepository
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedAuthorRepository
import com.kovhan.domain.library.SavedBookRepository
import com.kovhan.domain.settings.AppLanguage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("SaveDailyQuoteToFavouritesUseCase")
class SaveDailyQuoteToFavouritesUseCaseTest {

    private lateinit var collectionRepository: CollectionRepository
    private lateinit var authorRepository: SavedAuthorRepository
    private lateinit var bookRepository: SavedBookRepository
    private lateinit var quoteRepository: QuoteRepository
    private lateinit var idGenerator: IdGenerator
    private lateinit var useCase: SaveDailyQuoteToFavouritesUseCase

    private val daily = DailyQuote(
        id = "d1",
        textEn = "Know thyself",
        textUk = "Пізнай себе",
        authorEn = "Socrates",
        authorUk = "Сократ",
        bookEn = "Apology",
        bookUk = "Апологія",
    )

    @BeforeEach
    fun setUp() {
        collectionRepository = mockk(relaxed = true)
        authorRepository = mockk(relaxed = true)
        bookRepository = mockk(relaxed = true)
        quoteRepository = mockk(relaxed = true)
        idGenerator = mockk()
        useCase = SaveDailyQuoteToFavouritesUseCase(
            collectionRepository, authorRepository, bookRepository, quoteRepository, idGenerator,
        )
    }

    @Test
    @DisplayName("creates the favourites collection when it is missing")
    fun createsCollection() = runTest {
        coEvery { collectionRepository.getById(SavedCollection.FAVOURITES_ID) } returns null
        every { idGenerator.generate() } returnsMany listOf("a", "b", "q")

        useCase(daily, "Favourites", AppLanguage.ENGLISH)

        coVerify {
            collectionRepository.edit(
                SavedCollection(id = SavedCollection.FAVOURITES_ID, name = "Favourites"),
            )
        }
    }

    @Test
    @DisplayName("does not recreate the favourites collection when it exists")
    fun keepsExistingCollection() = runTest {
        coEvery { collectionRepository.getById(SavedCollection.FAVOURITES_ID) } returns
            SavedCollection(id = SavedCollection.FAVOURITES_ID, name = "Favourites")
        every { idGenerator.generate() } returnsMany listOf("a", "b", "q")

        useCase(daily, "Favourites", AppLanguage.ENGLISH)

        coVerify(exactly = 0) { collectionRepository.edit(any()) }
    }

    @Test
    @DisplayName("reuses an existing author matched by name ignoring case")
    fun reusesAuthor() = runTest {
        coEvery { collectionRepository.getById(any()) } returns
            SavedCollection(id = SavedCollection.FAVOURITES_ID, name = "Favourites")
        coEvery { authorRepository.getAll() } returns listOf(SavedAuthor("a-existing", "socrates"))
        coEvery { bookRepository.getAll() } returns emptyList()
        every { idGenerator.generate() } returnsMany listOf("book-id", "quote-id")

        val quote = slot<Quote>()
        coEvery { quoteRepository.edit(capture(quote)) } returns Unit

        useCase(daily, "Favourites", AppLanguage.ENGLISH)

        coVerify(exactly = 0) { authorRepository.edit(any()) }
        assertEquals("a-existing", quote.captured.authorId)
        assertEquals("book-id", quote.captured.bookId)
        assertEquals(SavedCollection.FAVOURITES_ID, quote.captured.collectionId)
    }

    @Test
    @DisplayName("creates author and book when none match and links them to the quote")
    fun createsAuthorAndBook() = runTest {
        coEvery { collectionRepository.getById(any()) } returns
            SavedCollection(id = SavedCollection.FAVOURITES_ID, name = "Favourites")
        coEvery { authorRepository.getAll() } returns emptyList()
        coEvery { bookRepository.getAll() } returns emptyList()
        every { idGenerator.generate() } returnsMany listOf("author-id", "book-id", "quote-id")

        val quote = slot<Quote>()
        coEvery { quoteRepository.edit(capture(quote)) } returns Unit

        useCase(daily, "Favourites", AppLanguage.ENGLISH)

        coVerify { authorRepository.edit(SavedAuthor(id = "author-id", name = "Socrates")) }
        coVerify { bookRepository.edit(SavedBook(id = "book-id", name = "Apology")) }
        assertEquals("author-id", quote.captured.authorId)
        assertEquals("book-id", quote.captured.bookId)
        assertEquals("quote-id", quote.captured.id)
        assertEquals("Know thyself", quote.captured.text)
        assertEquals("d1", quote.captured.sourceDailyId)
    }

    @Test
    @DisplayName("stores the Ukrainian text when the app language is Ukrainian")
    fun storesUkrainianText() = runTest {
        coEvery { collectionRepository.getById(any()) } returns
            SavedCollection(id = SavedCollection.FAVOURITES_ID, name = "Улюблене")
        coEvery { authorRepository.getAll() } returns emptyList()
        coEvery { bookRepository.getAll() } returns emptyList()
        every { idGenerator.generate() } returnsMany listOf("author-id", "book-id", "quote-id")

        val quote = slot<Quote>()
        coEvery { quoteRepository.edit(capture(quote)) } returns Unit

        useCase(daily, "Улюблене", AppLanguage.UKRAINIAN)

        assertEquals("Пізнай себе", quote.captured.text)
    }
}
