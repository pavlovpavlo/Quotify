package com.kovhan.feature.entitydetails.presentation.quote_edit.mvi

import com.kovhan.feature.entitydetails.presentation.entity_details.model.EntityQuoteDraft

interface EntityQuoteEditIntent {
    fun onDraftChanged(draft: EntityQuoteDraft)
    fun onOpenTagSheetRequested()
    fun onTagSheetApplied(selectedTags: List<String>, aiTags: List<String>)
    fun onSaveRequested()
}
