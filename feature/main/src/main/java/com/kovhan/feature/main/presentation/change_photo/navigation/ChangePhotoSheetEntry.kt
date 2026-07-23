package com.kovhan.feature.main.presentation.change_photo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.PhotoAction
import com.kovhan.feature.main.presentation.change_photo.ChangePhotoBottomSheet
import kotlinx.coroutines.launch

@Composable
internal fun ChangePhotoSheetEntry(coordinator: NavigationCoordinator) {
    val scope = rememberCoroutineScope()

    fun emit(action: PhotoAction) {
        scope.launch {
            coordinator.emitResult(NavigationCoordinator.KEY_PHOTO_ACTION, action)
            coordinator.dismissBottomSheet()
        }
    }

    ChangePhotoBottomSheet(
        onTakePhoto = { emit(PhotoAction.TAKE) },
        onPickGallery = { emit(PhotoAction.GALLERY) },
        onRemovePhoto = { emit(PhotoAction.REMOVE) },
        onDismiss = coordinator::dismissBottomSheet,
    )
}
