package com.kovhan.feature.entitydetails.presentation.entity_details.mvi

import com.kovhan.core.navigation.EntityType
import com.kovhan.core.navigation.QuoteRemovalMode
import com.kovhan.core.ui.UiEffect
import com.kovhan.feature.entitydetails.presentation.entity_details.model.EntityQuoteDraft

sealed interface EntityDetailsEffect : UiEffect {
    data object Close : EntityDetailsEffect

    data class OpenRenameSheet(
        val type: EntityType,
        val initialName: String,
    ) : EntityDetailsEffect

    data class OpenCollectionStyleSheet(
        val iconId: String,
        val tone: String,
    ) : EntityDetailsEffect

    data class OpenQuoteEditor(
        val draft: EntityQuoteDraft,
        val authorOptions: List<String>,
        val bookOptions: List<String>,
        val tagPool: List<String>,
    ) : EntityDetailsEffect

    data class OpenMoveQuoteSheet(
        val quoteId: String,
        val selectedCollectionId: String?,
        val excludedCollectionId: String?,
        val keepsFavourite: Boolean,
    ) : EntityDetailsEffect

    data class OpenDeleteEntityDialog(val type: EntityType) : EntityDetailsEffect

    data class OpenDeleteQuoteDialog(
        val quoteId: String,
        val mode: QuoteRemovalMode,
    ) : EntityDetailsEffect
}
