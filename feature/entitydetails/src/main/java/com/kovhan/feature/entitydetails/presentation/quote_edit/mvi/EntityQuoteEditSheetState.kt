package com.kovhan.feature.entitydetails.presentation.quote_edit.mvi

import com.kovhan.core.ui.UiState
import com.kovhan.feature.entitydetails.presentation.entity_details.model.EntityQuoteDraft

data class EntityQuoteEditSheetState(
    val draft: EntityQuoteDraft = EntityQuoteDraft(
        quoteId = "",
        text = "",
        authorName = "",
        bookName = "",
        tags = emptyList(),
        inWidgetPlaylist = false,
        inPushPlaylist = false,
    ),
    val authorOptions: List<String> = emptyList(),
    val bookOptions: List<String> = emptyList(),
    val tagPool: List<String> = emptyList(),
) : UiState
