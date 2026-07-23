package com.kovhan.feature.search.presentation.search.mvi

import com.kovhan.core.models.LibrarySearchResults
import com.kovhan.core.ui.UiEffect
import com.kovhan.core.ui.UiState

enum class SearchScope { QUOTES, FOLDERS, TAGS, BOOKS, AUTHORS }

data class SearchState(
    val isLoading: Boolean = false,
    val query: String = "",
    val scope: SearchScope = SearchScope.QUOTES,
    val results: LibrarySearchResults = LibrarySearchResults(),
) : UiState

sealed interface SearchEffect : UiEffect
