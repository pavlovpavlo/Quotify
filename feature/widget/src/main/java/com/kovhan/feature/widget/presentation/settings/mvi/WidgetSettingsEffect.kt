package com.kovhan.feature.widget.presentation.settings.mvi

import com.kovhan.core.ui.UiEffect

sealed interface WidgetSettingsEffect : UiEffect {
    data object OpenCreatePlaylist : WidgetSettingsEffect
    data class OpenEditPlaylist(val playlistId: String) : WidgetSettingsEffect
    data class OpenFrequencyPicker(val hours: Int) : WidgetSettingsEffect
    data object WidgetAdded : WidgetSettingsEffect
}
