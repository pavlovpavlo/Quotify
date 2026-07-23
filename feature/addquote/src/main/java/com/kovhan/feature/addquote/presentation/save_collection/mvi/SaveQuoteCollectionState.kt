package com.kovhan.feature.addquote.presentation.save_collection.mvi

import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.ui.UiEffect
import com.kovhan.core.ui.UiState

data class SaveQuoteCollectionState(
    val collections: List<SavedCollection> = emptyList(),
    val chosenCollectionId: String? = null,
    val savingCollectionId: String? = null,
) : UiState

sealed class SaveQuoteCollectionEffect : UiEffect {
    data object Saved : SaveQuoteCollectionEffect()
}
