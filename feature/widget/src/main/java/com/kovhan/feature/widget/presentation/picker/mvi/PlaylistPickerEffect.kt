package com.kovhan.feature.widget.presentation.picker.mvi

import com.kovhan.core.navigation.PlaylistNameMode
import com.kovhan.core.ui.UiEffect

sealed interface PlaylistPickerEffect : UiEffect {
    data class OpenNameSheet(val mode: PlaylistNameMode, val initialName: String) :
        PlaylistPickerEffect

    data class OpenDeleteDialog(val name: String) : PlaylistPickerEffect

    /** Playlist created / updated / deleted — return to the widget settings screen. */
    data object Saved : PlaylistPickerEffect
}
