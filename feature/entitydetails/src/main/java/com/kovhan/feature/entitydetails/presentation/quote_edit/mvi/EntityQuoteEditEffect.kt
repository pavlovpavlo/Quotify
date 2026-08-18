package com.kovhan.feature.entitydetails.presentation.quote_edit.mvi

import com.kovhan.core.ui.UiEffect
import com.kovhan.feature.entitydetails.presentation.entity_details.model.EntityQuoteDraft

sealed interface EntityQuoteEditEffect : UiEffect {
    data class OpenTagSheet(
        val quoteText: String,
        val selectedTags: List<String>,
        val aiTags: List<String>,
        val tagPool: List<String>,
    ) : EntityQuoteEditEffect

    data class CloseWithResult(val draft: EntityQuoteDraft) : EntityQuoteEditEffect
}
