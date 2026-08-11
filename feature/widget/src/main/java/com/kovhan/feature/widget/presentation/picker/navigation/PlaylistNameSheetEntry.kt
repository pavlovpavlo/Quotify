package com.kovhan.feature.widget.presentation.picker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.PlaylistNameMode
import com.kovhan.core.navigation.PlaylistNameSheetKey
import com.kovhan.core.ui.component.bottomsheet.RenameBottomSheet
import com.kovhan.design.systems.R as DsR
import kotlinx.coroutines.launch

@Composable
internal fun PlaylistNameSheetEntry(
    key: PlaylistNameSheetKey,
    coordinator: NavigationCoordinator,
) {
    val scope = rememberCoroutineScope()

    val titleRes = when (key.mode) {
        PlaylistNameMode.CREATE -> DsR.string.playlist_new_title
        PlaylistNameMode.RENAME -> DsR.string.playlist_rename_title
    }
    val confirmRes = when (key.mode) {
        PlaylistNameMode.CREATE -> DsR.string.playlist_create_confirm
        PlaylistNameMode.RENAME -> DsR.string.playlist_rename_confirm
    }
    val onBack: (() -> Unit)? = when (key.mode) {
        PlaylistNameMode.CREATE -> ({ coordinator.dismissBottomSheet() })
        PlaylistNameMode.RENAME -> null
    }

    RenameBottomSheet(
        title = stringResource(titleRes),
        label = stringResource(DsR.string.playlist_name_label),
        placeholder = stringResource(DsR.string.playlist_name_placeholder),
        initialName = key.initialName,
        confirmText = stringResource(confirmRes),
        onSave = { name ->
            scope.launch {
                coordinator.emitResult(NavigationCoordinator.KEY_PLAYLIST_NAME, name.trim())
                coordinator.dismissBottomSheet()
            }
        },
        onDismiss = coordinator::dismissBottomSheet,
        onBack = onBack,
    )
}
