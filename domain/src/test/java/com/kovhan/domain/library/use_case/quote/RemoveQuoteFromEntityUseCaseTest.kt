package com.kovhan.domain.library.use_case.quote

import com.kovhan.core.models.LibraryEntityType
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.models.quote.Quote
import com.kovhan.domain.library.CollectionRepository
import com.kovhan.domain.widget.use_case.content.HandleWidgetQuoteRemovalUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("RemoveQuoteFromEntityUseCase")
class RemoveQuoteFromEntityUseCaseTest {

    private lateinit var getQuoteById: GetQuoteByIdUseCase
    private lateinit var getQuotes: GetQuotesUseCase
    private lateinit var editQuote: EditQuoteUseCase
    private lateinit var deleteQuote: DeleteQuoteUseCase
    private lateinit var collectionRepository: CollectionRepository
    private lateinit var handleWidgetQuoteRemoval: HandleWidgetQuoteRemovalUseCase
    private lateinit var useCase: RemoveQuoteFromEntityUseCase

    @BeforeEach
    fun setUp() {
        getQuoteById = mockk()
        getQuotes = mockk()
        editQuote = mockk(relaxed = true)
        deleteQuote = mockk(relaxed = true)
        collectionRepository = mockk(relaxed = true)
        handleWidgetQuoteRemoval = mockk(relaxed = true)
        useCase = RemoveQuoteFromEntityUseCase(
            getQuoteById = getQuoteById,
            getQuotes = getQuotes,
            editQuote = editQuote,
            deleteQuote = deleteQuote,
            collectionRepository = collectionRepository,
            handleWidgetQuoteRemoval = handleWidgetQuoteRemoval,
        )
    }

    @Test
    @DisplayName("removing from favourites only clears the flag when the quote lives in a folder")
    fun clearsOnlyFavouriteFlag() = runTest {
        val quote = Quote(id = "q1", text = "x", collectionId = "c1", isFavourite = true)
        coEvery { getQuoteById("q1") } returns quote
        coEvery { getQuotes() } returns listOf(quote.copy(isFavourite = false))

        useCase(
            quoteId = "q1",
            entityType = LibraryEntityType.COLLECTION,
            entityId = SavedCollection.FAVOURITES_ID,
            generalName = "General",
        )

        coVerify { editQuote(quote.copy(isFavourite = false), "General") }
        coVerify(exactly = 0) { deleteQuote(any()) }
    }

    @Test
    @DisplayName("removing from a folder keeps a favourite quote in favourites")
    fun keepsFavouriteWhenRemovedFromFolder() = runTest {
        val quote = Quote(id = "q1", text = "x", collectionId = "c1", isFavourite = true)
        coEvery { getQuoteById("q1") } returns quote

        useCase(
            quoteId = "q1",
            entityType = LibraryEntityType.COLLECTION,
            entityId = "c1",
            generalName = "General",
        )

        coVerify { editQuote(quote.copy(collectionId = null), "General") }
        coVerify(exactly = 0) { deleteQuote(any()) }
    }

    @Test
    @DisplayName("removing from a folder deletes a quote that is not in favourites")
    fun deletesFolderOnlyQuote() = runTest {
        val quote = Quote(id = "q1", text = "x", collectionId = "c1", isFavourite = false)
        coEvery { getQuoteById("q1") } returns quote

        useCase(
            quoteId = "q1",
            entityType = LibraryEntityType.COLLECTION,
            entityId = "c1",
            generalName = "General",
        )

        coVerify { deleteQuote("q1") }
        coVerify(exactly = 0) { editQuote(any(), any()) }
    }

    @Test
    @DisplayName("removing from the general collection keeps a favourite quote in favourites")
    fun keepsFavouriteWhenRemovedFromGeneral() = runTest {
        val quote = Quote(
            id = "q1",
            text = "x",
            collectionId = SavedCollection.GENERAL_ID,
            isFavourite = true,
        )
        coEvery { getQuoteById("q1") } returns quote

        useCase(
            quoteId = "q1",
            entityType = LibraryEntityType.COLLECTION,
            entityId = SavedCollection.GENERAL_ID,
            generalName = "General",
        )

        coVerify { editQuote(quote.copy(collectionId = null), "General") }
        coVerify(exactly = 0) { deleteQuote(any()) }
    }

    @Test
    @DisplayName("removing from favourites deletes a quote that lives nowhere else")
    fun deletesFavouriteOnlyQuote() = runTest {
        val quote = Quote(id = "q1", text = "x", collectionId = null, isFavourite = true)
        coEvery { getQuoteById("q1") } returns quote
        coEvery { getQuotes() } returns emptyList()

        useCase(
            quoteId = "q1",
            entityType = LibraryEntityType.COLLECTION,
            entityId = SavedCollection.FAVOURITES_ID,
            generalName = "General",
        )

        coVerify { deleteQuote("q1") }
        coVerify { collectionRepository.deleteById(SavedCollection.FAVOURITES_ID) }
    }
}
