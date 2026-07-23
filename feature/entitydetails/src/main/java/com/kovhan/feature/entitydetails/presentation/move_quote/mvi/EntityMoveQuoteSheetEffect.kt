package com.kovhan.feature.entitydetails.presentation.move_quote.mvi

import com.kovhan.core.ui.UiEffect

sealed interface EntityMoveQuoteSheetEffect : UiEffect {
    data object OpenNewCollectionSheet : EntityMoveQuoteSheetEffect

    data class CloseWithResult(
        val quoteId: String,
        val targetCollectionId: String,
    ) : EntityMoveQuoteSheetEffect
}
