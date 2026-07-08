package com.kovhan.feature.addquote.presentation.details.component.tageditor

import com.kovhan.core.ui.UiState
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.ai.AiAccess
import com.kovhan.domain.ai.use_case.CheckAiAccessUseCase
import com.kovhan.domain.ai.use_case.RecordAiRequestUseCase
import com.kovhan.domain.ai.use_case.SuggestTagsUseCase
import com.kovhan.feature.addquote.presentation.details.mvi.AiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TagSheetUiState(
    val quoteText: String = "",
    val selectedTags: List<String> = emptyList(),
    val tagPool: List<String> = emptyList(),
    val tagQuery: String = "",
    val aiState: AiState = AiState.IDLE,
    val aiTags: List<String> = emptyList(),
) : UiState {
    val recentTags: List<String>
        get() = (tagPool + selectedTags).distinctBy { it.lowercase() }.matching(tagQuery)

    val filteredAiTags: List<String>
        get() = aiTags.matching(tagQuery)

    val canCreateTag: Boolean
        get() {
            val query = tagQuery.trim()
            if (query.isEmpty()) return false
            return (tagPool + selectedTags + aiTags).none { it.equals(query, ignoreCase = true) }
        }

    fun isTagSelected(tag: String): Boolean = selectedTags.any { it.equals(tag, ignoreCase = true) }
}

private fun List<String>.matching(query: String): List<String> {
    val q = query.trim()
    return if (q.isEmpty()) this else filter { it.contains(q, ignoreCase = true) }
}

@HiltViewModel
class TagSheetViewModel @Inject constructor(
    private val suggestTags: SuggestTagsUseCase,
    private val checkAiAccess: CheckAiAccessUseCase,
    private val recordAiRequest: RecordAiRequestUseCase,
) : BaseViewModel<TagSheetUiState, TagSheetEffect>(TagSheetUiState()) {

    fun initialize(
        quoteText: String,
        selectedTags: List<String>,
        tagPool: List<String>,
        aiTags: List<String>,
    ) = publishState {
        copy(
            quoteText = quoteText,
            selectedTags = selectedTags,
            tagPool = tagPool,
            aiTags = aiTags,
            aiState = if (aiTags.isEmpty()) AiState.IDLE else AiState.DONE,
        )
    }

    fun onQueryChange(value: String) = publishState { copy(tagQuery = value) }

    fun onToggleTag(tag: String) = publishState {
        val selected = if (isTagSelected(tag)) {
            selectedTags.filterNot { it.equals(tag, ignoreCase = true) }
        } else {
            selectedTags + tag
        }
        copy(selectedTags = selected)
    }

    fun onCreateTag() = publishState {
        val name = tagQuery.trim()
        if (name.isEmpty() || isTagSelected(name)) {
            copy(tagQuery = "")
        } else {
            copy(selectedTags = selectedTags + name, tagQuery = "")
        }
    }

    fun onGenerateAiTags() {
        val quote = uiState.value.quoteText.trim()
        if (quote.isBlank() || uiState.value.aiState == AiState.LOADING) return
        viewModelScope.launch {
            when (val access = checkAiAccess()) {
                is AiAccess.Denied -> publishEffect(TagSheetEffect.ShowAiLimitDialog(access.reason))
                AiAccess.Allowed -> {
                    publishState { copy(aiState = AiState.LOADING) }
                    val tags = suggestTags(quote)
                    recordAiRequest()
                    publishState { copy(aiState = AiState.DONE, aiTags = tags) }
                }
            }
        }
    }

    fun buildResult(): TagSheetResult = TagSheetResult(
        selectedTags = uiState.value.selectedTags,
        aiTags = uiState.value.aiTags,
    )
}
