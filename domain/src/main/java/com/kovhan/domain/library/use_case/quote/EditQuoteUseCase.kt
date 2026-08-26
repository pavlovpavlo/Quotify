package com.kovhan.domain.library.use_case.quote

import com.kovhan.core.models.quote.Quote
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.use_case.collection.EnsureGeneralCollectionUseCase
import javax.inject.Inject

class EditQuoteUseCase @Inject constructor(
    private val repository: QuoteRepository,
    private val ensureGeneralCollection: EnsureGeneralCollectionUseCase,
) {
    suspend operator fun invoke(quote: Quote, generalName: String) {
        val needsGeneral = quote.collectionId.isNullOrBlank() && !quote.isFavourite
        val resolved = if (needsGeneral) {
            quote.copy(collectionId = SavedCollection.GENERAL_ID)
        } else {
            quote
        }
        // Папку треба гарантувати за підсумковим collectionId, а не лише коли ми
        // самі підставили «Загальну»: у шиті переміщення вона може бути вибрана
        // явно, і тоді рядка колекції ще не існує — цитата б поїхала в папку,
        // якої немає, і зникла б з бібліотеки.
        if (resolved.collectionId == SavedCollection.GENERAL_ID) {
            ensureGeneralCollection(generalName)
        }
        repository.edit(resolved)
    }
}
