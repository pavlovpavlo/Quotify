package com.kovhan.feature.widget.presentation.picker.mvi

import com.kovhan.core.models.LibrarySearchResults
import com.kovhan.core.models.widget.PlaylistSource
import com.kovhan.core.ui.UiState

enum class PickerScope { QUOTES, FOLDERS, TAGS, BOOKS, AUTHORS }

enum class PickerMode { CREATE, EDIT }

data class PlaylistPickerState(
    val isLoading: Boolean = true,
    val mode: PickerMode = PickerMode.CREATE,
    val playlistId: String? = null,
    val name: String = "",
    val query: String = "",
    val scope: PickerScope = PickerScope.QUOTES,
    val results: LibrarySearchResults = LibrarySearchResults(),
    val picked: Set<PlaylistSource> = emptySet(),
    val menuVisible: Boolean = false,
) : UiState {
    val canSave: Boolean get() = picked.isNotEmpty()
}
