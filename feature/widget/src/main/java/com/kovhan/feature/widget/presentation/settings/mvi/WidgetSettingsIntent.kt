package com.kovhan.feature.widget.presentation.settings.mvi

import com.kovhan.core.models.widget.WidgetFeedback
import com.kovhan.core.models.widget.WidgetSource
import com.kovhan.core.models.widget.WidgetStyle

interface WidgetSettingsIntent {
    fun onSourceSelected(source: WidgetSource)
    fun onCreatePlaylistClicked()
    fun onEditPlaylistClicked(playlistId: String)
    fun onDailyQuoteToggled(enabled: Boolean)
    fun onFrequencyClicked()
    fun onStyleSelected(style: WidgetStyle)
    fun onEditStyleClicked(style: WidgetStyle)
    fun onFeedbackSelected(feedback: WidgetFeedback)
    fun onAddToHomeClicked()
    fun onBackClicked(widgetPlaced: Boolean)

    companion object {
        val Empty: WidgetSettingsIntent = object : WidgetSettingsIntent {
            override fun onSourceSelected(source: WidgetSource) = Unit
            override fun onCreatePlaylistClicked() = Unit
            override fun onEditPlaylistClicked(playlistId: String) = Unit
            override fun onDailyQuoteToggled(enabled: Boolean) = Unit
            override fun onFrequencyClicked() = Unit
            override fun onStyleSelected(style: WidgetStyle) = Unit
            override fun onEditStyleClicked(style: WidgetStyle) = Unit
            override fun onFeedbackSelected(feedback: WidgetFeedback) = Unit
            override fun onAddToHomeClicked() = Unit
            override fun onBackClicked(widgetPlaced: Boolean) = Unit
        }
    }
}
