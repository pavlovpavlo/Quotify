package com.kovhan.feature.widget.presentation.settings.mvi

import com.kovhan.core.models.widget.WidgetSource
import com.kovhan.core.models.widget.WidgetStyle

interface WidgetSettingsIntent {
    fun onSourceSelected(source: WidgetSource)
    fun onCreatePlaylistClicked()
    fun onEditPlaylistClicked(playlistId: String)
    fun onDailyQuoteToggled(enabled: Boolean)
    fun onFrequencyClicked()
    fun onStyleSelected(style: WidgetStyle)
    fun onAddToHomeClicked()

    companion object {
        val Empty: WidgetSettingsIntent = object : WidgetSettingsIntent {
            override fun onSourceSelected(source: WidgetSource) = Unit
            override fun onCreatePlaylistClicked() = Unit
            override fun onEditPlaylistClicked(playlistId: String) = Unit
            override fun onDailyQuoteToggled(enabled: Boolean) = Unit
            override fun onFrequencyClicked() = Unit
            override fun onStyleSelected(style: WidgetStyle) = Unit
            override fun onAddToHomeClicked() = Unit
        }
    }
}
