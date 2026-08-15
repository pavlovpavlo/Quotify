package com.kovhan.feature.widget.presentation.settings.mvi

import com.kovhan.core.models.widget.PlaylistWithCount
import com.kovhan.core.models.widget.WidgetAppearance
import com.kovhan.core.models.widget.WidgetSettings
import com.kovhan.core.models.widget.WidgetSource
import com.kovhan.core.models.widget.WidgetStyle
import com.kovhan.core.ui.UiState

data class WidgetSettingsState(
    val isLoading: Boolean = true,
    val selectedSource: WidgetSource = WidgetSource.All,
    val allCount: Int = 0,
    val favouritesCount: Int = 0,
    val playlists: List<PlaylistWithCount> = emptyList(),
    val includeDailyQuote: Boolean = true,
    val frequencyHours: Int = WidgetSettings.DEFAULT_FREQUENCY_HOURS,
    val style: WidgetStyle = WidgetStyle.CLASSIC,
    val appearance: WidgetAppearance = WidgetAppearance(),
    val feedbackGiven: Boolean = true,
    val hasPendingChanges: Boolean = false,
) : UiState
