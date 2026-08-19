package com.kovhan.feature.widget.presentation.settings.navigation

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.models.feedback.FeedbackSource
import com.kovhan.core.navigation.FeedbackDialogKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.PlaylistPickerKey
import com.kovhan.core.navigation.WidgetAppearanceKey
import com.kovhan.core.navigation.WidgetExitAction
import com.kovhan.core.navigation.WidgetFrequencySheetKey
import com.kovhan.core.navigation.WidgetSettingsExitDialogKey
import com.kovhan.core.ui.snackbar.AppSnackbarBus
import com.kovhan.core.ui.snackbar.SnackbarMessage
import com.kovhan.core.ui.widget.HomeWidgetPresence
import com.kovhan.design.systems.R as DsR
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
            }
    }

    LaunchedEffect(Unit) {
        coordinator.clearResult(NavigationCoordinator.KEY_WIDGET_SETTINGS_EXIT)
        coordinator.observeResult<WidgetExitAction>(
            NavigationCoordinator.KEY_WIDGET_SETTINGS_EXIT,
        ).collect { action ->
            action ?: return@collect
            viewModel.onExitAction(action)
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

                is WidgetSettingsEffect.OpenAppearanceEditor ->
                    coordinator.navigate(WidgetAppearanceKey(effect.style.name))

                is WidgetSettingsEffect.OpenFeedback ->
                    coordinator.showDialog(
                        FeedbackDialogKey(
                            source = FeedbackSource.WIDGET,
                            liked = effect.liked,
                        ),
                    )

                WidgetSettingsEffect.WidgetAdded ->
                    if (applyWidget(context, viewModel)) coordinator.goBack()

                WidgetSettingsEffect.ApplyAndClose -> {
                    applyWidget(context, viewModel)
                    coordinator.goBack()
                }

                WidgetSettingsEffect.ConfirmExit ->
                    coordinator.showDialog(WidgetSettingsExitDialogKey)

                WidgetSettingsEffect.Close -> coordinator.goBack()
            }
        }
    }

    WidgetSettingsScreen(
        state = state.value,
        intent = viewModel,
        onBack = { viewModel.onBackClicked(HomeWidgetPresence.isPlaced(context)) },
        paddingValues = paddingValues,
    )
}

/** Returns whether the widget is now carrying the saved settings. */
private suspend fun applyWidget(
    context: Context,
    viewModel: WidgetSettingsViewModel,
): Boolean = when {
    HomeWidgetPresence.isPlaced(context) -> {
        viewModel.applyToWidget(alreadyPlaced = true)
        WidgetRotationScheduler.reschedule(context, viewModel.uiState.value.frequencyHours)
        AppSnackbarBus.show(SnackbarMessage.success(DsR.string.widget_updated_message))
        true
    }

    WidgetPinner.pin(context) -> {
        viewModel.applyToWidget(alreadyPlaced = false)
        AppSnackbarBus.show(SnackbarMessage.success(DsR.string.widget_added_message))
        true
    }

    else -> {
        AppSnackbarBus.show(SnackbarMessage.info(DsR.string.widget_pin_unsupported))
        false
    }
}
