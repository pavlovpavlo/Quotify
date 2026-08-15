package com.kovhan.feature.widget.presentation.settings.mvi

import com.kovhan.core.models.widget.WidgetStyle
import com.kovhan.core.ui.UiEffect

sealed interface WidgetSettingsEffect : UiEffect {
    data object OpenCreatePlaylist : WidgetSettingsEffect
    data class OpenEditPlaylist(val playlistId: String) : WidgetSettingsEffect
    data class OpenFrequencyPicker(val hours: Int) : WidgetSettingsEffect
    data class OpenAppearanceEditor(val style: WidgetStyle) : WidgetSettingsEffect
    data class OpenFeedback(val liked: Boolean) : WidgetSettingsEffect
    data object WidgetAdded : WidgetSettingsEffect
    data object ApplyAndClose : WidgetSettingsEffect
    data object ConfirmExit : WidgetSettingsEffect
    data object Close : WidgetSettingsEffect
}
