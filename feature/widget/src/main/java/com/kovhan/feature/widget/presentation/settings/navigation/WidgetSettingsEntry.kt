package com.kovhan.feature.widget.presentation.settings.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.PlaylistPickerKey
import com.kovhan.core.navigation.WidgetFrequencySheetKey
import com.kovhan.core.ui.snackbar.AppSnackbarBus
import com.kovhan.core.ui.snackbar.SnackbarMessage
import androidx.glance.appwidget.updateAll
import com.kovhan.core.ui.widget.HomeWidgetPresence
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.widget.glance.QuotifyGlanceWidget
import com.kovhan.feature.widget.glance.WidgetPinner
import com.kovhan.feature.widget.glance.WidgetRotationScheduler
import com.kovhan.feature.widget.presentation.settings.WidgetSettingsScreen
import com.kovhan.feature.widget.presentation.settings.WidgetSettingsViewModel
import com.kovhan.feature.widget.presentation.settings.mvi.WidgetSettingsEffect

@Composable
internal fun WidgetSettingsEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<WidgetSettingsViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        coordinator.clearResult(NavigationCoordinator.KEY_WIDGET_FREQUENCY)
        coordinator.observeResult<Int>(NavigationCoordinator.KEY_WIDGET_FREQUENCY)
            .collect { hours ->
                hours ?: return@collect
                viewModel.applyFrequency(hours)
                if (HomeWidgetPresence.isPlaced(context)) {
                    WidgetRotationScheduler.reschedule(context, hours)
                }
            }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                WidgetSettingsEffect.OpenCreatePlaylist ->
                    coordinator.navigate(PlaylistPickerKey())

                is WidgetSettingsEffect.OpenEditPlaylist ->
                    coordinator.navigate(PlaylistPickerKey(effect.playlistId))

                is WidgetSettingsEffect.OpenFrequencyPicker ->
                    coordinator.showBottomSheet(WidgetFrequencySheetKey(effect.hours))

                WidgetSettingsEffect.WidgetAdded -> when {
                    // Already on the home screen → just refresh it.
                    HomeWidgetPresence.isPlaced(context) -> {
                        QuotifyGlanceWidget().updateAll(context)
                        AppSnackbarBus.show(SnackbarMessage.success(DsR.string.widget_updated_message))
                        coordinator.goBack()
                    }
                    // Not placed → ask the launcher to pin it.
                    WidgetPinner.pin(context) -> {
                        AppSnackbarBus.show(SnackbarMessage.success(DsR.string.widget_added_message))
                        coordinator.goBack()
                    }
                    // Launcher can't pin programmatically → tell the user to add manually.
                    else ->
                        AppSnackbarBus.show(SnackbarMessage.info(DsR.string.widget_pin_unsupported))
                }
            }
        }
    }

    WidgetSettingsScreen(
        state = state.value,
        intent = viewModel,
        onBack = coordinator::goBack,
        paddingValues = paddingValues,
    )
}
