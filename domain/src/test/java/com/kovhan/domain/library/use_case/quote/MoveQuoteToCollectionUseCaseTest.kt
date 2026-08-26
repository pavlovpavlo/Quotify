package com.kovhan.domain.library.use_case.quote

import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.models.quote.Quote
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("MoveQuoteToCollectionUseCase")
class MoveQuoteToCollectionUseCaseTest {

    private lateinit var getQuoteById: GetQuoteByIdUseCase
    private lateinit var editQuote: EditQuoteUseCase
    private lateinit var useCase: MoveQuoteToCollectionUseCase

    @BeforeEach
    fun setUp() {
        getQuoteById = mockk()
        editQuote = mockk(relaxed = true)
        useCase = MoveQuoteToCollectionUseCase(getQuoteById, editQuote)
    }

    @Test
    @DisplayName("moving a favourite lands it in the folder and keeps the favourite flag")
    fun keepsFavouriteFlag() = runTest {
        val quote = Quote(id = "q1", text = "x", collectionId = null, isFavourite = true)
        coEvery { getQuoteById("q1") } returns quote

        useCase(quoteId = "q1", targetCollectionId = "c1", generalName = "General")

        coVerify { editQuote(quote.copy(collectionId = "c1"), "General") }
    }

    @Test
    @DisplayName("moving a favourite into the general collection keeps the favourite flag")
    fun keepsFavouriteFlagWhenMovingToGeneral() = runTest {
        val quote = Quote(id = "q1", text = "x", collectionId = null, isFavourite = true)
        coEvery { getQuoteById("q1") } returns quote

        useCase(
            quoteId = "q1",
            targetCollectionId = SavedCollection.GENERAL_ID,
            generalName = "General",
        )

        coVerify {
            editQuote(quote.copy(collectionId = SavedCollection.GENERAL_ID), "General")
        }
    }

    @Test
    @DisplayName("does nothing when the quote is gone")
    fun ignoresMissingQuote() = runTest {
        coEvery { getQuoteById("q1") } returns null

        useCase(quoteId = "q1", targetCollectionId = "c1", generalName = "General")

        coVerify(exactly = 0) { editQuote(any(), any()) }
    }
}
