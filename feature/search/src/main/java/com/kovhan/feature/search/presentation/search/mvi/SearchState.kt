package com.kovhan.feature.search.presentation.search.mvi

import com.kovhan.core.models.LibrarySearchResults
import com.kovhan.core.navigation.QuoteEditDraft
import com.kovhan.core.ui.UiEffect
import com.kovhan.core.ui.UiState

enum class SearchScope { QUOTES, FOLDERS, TAGS, BOOKS, AUTHORS }

data class SearchState(
    val isLoading: Boolean = false,
    val query: String = "",
    val scope: SearchScope = SearchScope.QUOTES,
    val results: LibrarySearchResults = LibrarySearchResults(),
    val expandedQuoteMenuId: String? = null,
) : UiState

sealed interface SearchEffect : UiEffect {

    data class OpenQuoteEditor(
        val draft: QuoteEditDraft,
        val authorOptions: List<String>,
        val bookOptions: List<String>,
        val tagPool: List<String>,
    ) : SearchEffect

    data class OpenMoveQuoteSheet(
        val quoteId: String,
        val keepsFavourite: Boolean,
    ) : SearchEffect

    data class OpenDeleteQuoteDialog(val quoteId: String) : SearchEffect
}
