package com.kovhan.feature.addquote.presentation.save_collection.mvi

import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.models.quote.AddQuoteAction
import com.kovhan.core.ui.UiEffect
import com.kovhan.core.ui.UiState

data class SaveQuoteCollectionState(
    val collections: List<SavedCollection> = emptyList(),
    val chosenCollectionId: String? = null,
    val savingCollectionId: String? = null,
) : UiState

sealed class SaveQuoteCollectionEffect : UiEffect {
    data class Saved(val action: AddQuoteAction) : SaveQuoteCollectionEffect()

    /** Безкоштовний план вичерпав ліміт збережених цитат. */
    data object ShowPaywall : SaveQuoteCollectionEffect()
}
