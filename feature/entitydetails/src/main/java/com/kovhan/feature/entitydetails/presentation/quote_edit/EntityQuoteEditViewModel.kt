package com.kovhan.feature.entitydetails.presentation.quote_edit

import com.kovhan.core.navigation.EditQuoteKey
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.feature.entitydetails.presentation.entity_details.model.EntityQuoteDraft
import com.kovhan.feature.entitydetails.presentation.quote_edit.mvi.EntityQuoteEditEffect
import com.kovhan.feature.entitydetails.presentation.quote_edit.mvi.EntityQuoteEditIntent
import com.kovhan.feature.entitydetails.presentation.quote_edit.mvi.EntityQuoteEditState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EntityQuoteEditViewModel @Inject constructor() :
    BaseViewModel<EntityQuoteEditState, EntityQuoteEditEffect>(EntityQuoteEditState()),
    EntityQuoteEditIntent {

    private var bound = false

    fun bind(key: EditQuoteKey) {
        if (bound) return
        bound = true
        publishState {
            EntityQuoteEditState(
                draft = EntityQuoteDraft(
                    quoteId = key.quoteId,
                    text = key.text,
                    authorName = key.authorName,
                    bookName = key.bookName,
                    tags = key.tags,
                    aiTags = key.aiTags,
                    inWidgetPlaylist = key.inWidgetPlaylist,
                    inPushPlaylist = key.inPushPlaylist,
                    page = key.page,
                ),
                authorOptions = key.authorOptions,
                bookOptions = key.bookOptions,
                tagPool = key.tagPool,
            )
        }
    }

    override fun onDraftChanged(draft: EntityQuoteDraft) {
        publishState { copy(draft = draft) }
    }

    override fun onOpenTagSheetRequested() {
        val state = uiState.value
        publishEffect(
            EntityQuoteEditEffect.OpenTagSheet(
                quoteText = state.draft.text,
                selectedTags = state.draft.tags,
                aiTags = state.draft.aiTags,
                tagPool = state.tagPool,
            ),
        )
    }

    override fun onTagSheetApplied(selectedTags: List<String>, aiTags: List<String>) {
        publishState {
            copy(
                draft = draft.copy(
                    tags = selectedTags,
                    aiTags = aiTags,
                ),
            )
        }
    }

    override fun onSaveRequested() {
        val draft = uiState.value.draft
        if (draft.text.isBlank()) return
        publishEffect(EntityQuoteEditEffect.CloseWithResult(draft))
    }
}
