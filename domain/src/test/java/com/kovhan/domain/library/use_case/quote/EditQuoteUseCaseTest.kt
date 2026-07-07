package com.kovhan.domain.library.use_case.quote

import com.kovhan.core.models.Quote
import com.kovhan.core.models.SavedCollection
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.use_case.collection.EnsureGeneralCollectionUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("EditQuoteUseCase")
class EditQuoteUseCaseTest {

    private lateinit var repository: QuoteRepository
    private lateinit var ensureGeneralCollection: EnsureGeneralCollectionUseCase
    private lateinit var useCase: EditQuoteUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        ensureGeneralCollection = mockk(relaxed = true)
        useCase = EditQuoteUseCase(repository, ensureGeneralCollection)
    }

    @Test
    @DisplayName("defaults a quote without a collection into the general collection")
    fun defaultsToGeneral() = runTest {
        val quote = Quote(id = "q1", text = "x", collectionId = null)

        useCase(quote, "General")

        coVerify { ensureGeneralCollection("General") }
        coVerify { repository.edit(quote.copy(collectionId = SavedCollection.GENERAL_ID)) }
    }

    @Test
    @DisplayName("keeps the chosen collection untouched")
    fun keepsChosenCollection() = runTest {
        val quote = Quote(id = "q1", text = "x", collectionId = "c1")

        useCase(quote, "General")

        coVerify(exactly = 0) { ensureGeneralCollection(any()) }
        coVerify { repository.edit(quote) }
    }
}
