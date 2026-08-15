package com.kovhan.feature.widget.presentation.appearance.mvi

import com.kovhan.core.ui.UiEffect

sealed interface WidgetAppearanceEffect : UiEffect {
    data object Saved : WidgetAppearanceEffect
}
