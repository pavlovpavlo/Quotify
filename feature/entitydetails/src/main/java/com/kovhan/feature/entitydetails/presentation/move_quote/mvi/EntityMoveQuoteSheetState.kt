package com.kovhan.feature.entitydetails.presentation.move_quote.mvi

import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.ui.UiState

data class EntityMoveQuoteSheetState(
    val quoteId: String = "",
    val targets: List<SavedCollection> = emptyList(),
    val selectedCollectionId: String? = null,
    val keepsFavourite: Boolean = false,
) : UiState
