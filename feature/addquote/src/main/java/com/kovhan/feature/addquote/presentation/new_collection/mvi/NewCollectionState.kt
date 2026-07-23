package com.kovhan.feature.addquote.presentation.new_collection.mvi

import com.kovhan.core.ui.UiEffect
import com.kovhan.core.ui.UiState

data class NewCollectionState(
    val name: String = "",
    val isSaving: Boolean = false,
) : UiState {
    val canSave: Boolean
        get() = name.trim().isNotEmpty() && !isSaving
}

sealed class NewCollectionEffect : UiEffect {
    data class Saved(val collectionId: String) : NewCollectionEffect()
}
