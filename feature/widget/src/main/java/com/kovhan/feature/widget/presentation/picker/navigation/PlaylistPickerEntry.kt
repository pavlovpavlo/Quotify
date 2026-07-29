package com.kovhan.feature.widget.presentation.picker.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.ConfirmDialogKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.PlaylistNameSheetKey
import com.kovhan.core.navigation.PlaylistPickerKey
import com.kovhan.core.navigation.WidgetSettingsKey
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.widget.presentation.picker.PlaylistPickerScreen
import com.kovhan.feature.widget.presentation.picker.PlaylistPickerViewModel
import com.kovhan.feature.widget.presentation.picker.mvi.PlaylistPickerEffect

@Composable
internal fun PlaylistPickerEntry(
    key: PlaylistPickerKey,
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<PlaylistPickerViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key.playlistId) {
        viewModel.initialize(key.playlistId)
    }

    LaunchedEffect(Unit) {
        coordinator.clearResult(NavigationCoordinator.KEY_PLAYLIST_NAME)
        coordinator.observeResult<String>(NavigationCoordinator.KEY_PLAYLIST_NAME)
            .collect { name -> name?.let(viewModel::onNameConfirmed) }
    }

    LaunchedEffect(Unit) {
        coordinator.clearResult(NavigationCoordinator.KEY_PLAYLIST_DELETE)
        coordinator.observeResult<Boolean>(NavigationCoordinator.KEY_PLAYLIST_DELETE)
            .collect { confirmed -> if (confirmed == true) viewModel.onDeleteConfirmed() }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is PlaylistPickerEffect.OpenNameSheet ->
                    coordinator.showBottomSheet(
                        PlaylistNameSheetKey(mode = effect.mode, initialName = effect.initialName),
                    )

                is PlaylistPickerEffect.OpenDeleteDialog ->
                    coordinator.showDialog(
                        ConfirmDialogKey(
                            iconRes = DsR.drawable.ic_trash,
                            titleRes = DsR.string.playlist_delete_title,
                            messageRes = DsR.string.playlist_delete_message,
                            confirmRes = DsR.string.playlist_delete_confirm,
                            cancelRes = DsR.string.dialog_cancel,
                            resultKey = NavigationCoordinator.KEY_PLAYLIST_DELETE,
                        ),
                    )

                PlaylistPickerEffect.Saved -> coordinator.popBackTo(WidgetSettingsKey)
            }
        }
    }

    PlaylistPickerScreen(
        state = state.value,
        intent = viewModel,
        onBack = coordinator::goBack,
        paddingValues = paddingValues,
    )
}
