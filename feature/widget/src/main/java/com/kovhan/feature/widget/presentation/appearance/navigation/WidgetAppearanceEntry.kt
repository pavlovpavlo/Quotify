package com.kovhan.feature.widget.presentation.appearance.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.models.widget.WidgetStyle
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.WidgetAppearanceKey
import com.kovhan.feature.widget.presentation.appearance.WidgetAppearanceScreen
import com.kovhan.feature.widget.presentation.appearance.WidgetAppearanceViewModel

@Composable
internal fun WidgetAppearanceEntry(
    key: WidgetAppearanceKey,
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<WidgetAppearanceViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key.style) {
        val style = runCatching { WidgetStyle.valueOf(key.style) }.getOrDefault(WidgetStyle.CLASSIC)
        viewModel.initialize(style)
    }

    WidgetAppearanceScreen(
        state = state.value,
        intent = viewModel,
        onBack = coordinator::goBack,
        onSave = coordinator::goBack,
        paddingValues = paddingValues,
    )
}
