package com.kovhan.feature.widget.presentation.appearance.mvi

import com.kovhan.core.models.widget.WidgetStyle
import com.kovhan.core.models.widget.WidgetStyleSettings
import com.kovhan.core.ui.UiState

data class WidgetAppearanceState(
    val isLoading: Boolean = true,
    val settings: WidgetStyleSettings = WidgetStyleSettings.Classic(),
    val pickingCustomTextColor: Boolean = false,
    val unlockedCovers: List<String> = emptyList(),
) : UiState {
    val style: WidgetStyle get() = settings.style
}
