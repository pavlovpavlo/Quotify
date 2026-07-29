package com.kovhan.feature.widget.presentation.picker.mvi

import com.kovhan.core.models.widget.PlaylistSource

interface PlaylistPickerIntent {
    fun onQueryChange(query: String)
    fun onScopeChange(scope: PickerScope)
    fun onTogglePick(source: PlaylistSource)
    fun onSaveClicked()
    fun onToggleMenu()
    fun onRenameClicked()
    fun onDeleteClicked()
    fun onDismissMenu()

    companion object {
        val Empty: PlaylistPickerIntent = object : PlaylistPickerIntent {
            override fun onQueryChange(query: String) = Unit
            override fun onScopeChange(scope: PickerScope) = Unit
            override fun onTogglePick(source: PlaylistSource) = Unit
            override fun onSaveClicked() = Unit
            override fun onToggleMenu() = Unit
            override fun onRenameClicked() = Unit
            override fun onDeleteClicked() = Unit
            override fun onDismissMenu() = Unit
        }
    }
}
