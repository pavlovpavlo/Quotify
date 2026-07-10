package com.kovhan.feature.main.presentation.edit_profile.navigation

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.ChangePhotoSheetKey
import com.kovhan.core.navigation.CompleteKey
import com.kovhan.core.navigation.DeleteAccountDialogKey
import com.kovhan.core.navigation.EditField
import com.kovhan.core.navigation.EditFieldSheetKey
import com.kovhan.core.navigation.LoginKey
import com.kovhan.core.navigation.LogoutDialogKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.PhotoAction
import com.kovhan.core.ui.snackbar.SnackbarMessageEffect
import com.kovhan.feature.main.presentation.edit_profile.EditProfileScreen
import com.kovhan.feature.main.presentation.edit_profile.EditProfileViewModel
import com.kovhan.feature.main.presentation.edit_profile.mvi.EditProfileEffect
import java.io.File

@Composable
internal fun EditProfileEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<EditProfileViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarMessageEffect(viewModel.snackbar, snackbarHostState)

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        uri?.let { viewModel.onPhotoPicked(it.toString()) }
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview(),
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            val file = File(context.cacheDir, "camera_${System.currentTimeMillis()}.jpg")
            file.outputStream().use { os -> it.compress(Bitmap.CompressFormat.JPEG, 90, os) }
            viewModel.onPhotoPicked(Uri.fromFile(file).toString())
        }
    }

    LaunchedEffect(Unit) {
        coordinator.observeResult<PhotoAction>(NavigationCoordinator.KEY_PHOTO_ACTION)
            .collect { action ->
                when (action) {
                    PhotoAction.TAKE -> cameraLauncher.launch(null)
                    PhotoAction.GALLERY -> galleryLauncher.launch("image/*")
                    PhotoAction.REMOVE -> viewModel.onPhotoRemoved()
                    null -> Unit
                }
            }
    }
    LaunchedEffect(Unit) {
        coordinator.observeResult<Pair<EditField, String>>(NavigationCoordinator.KEY_EDIT_FIELD_VALUE)
            .collect { result -> result?.let { (field, value) -> viewModel.onFieldSaved(field, value) } }
    }
    LaunchedEffect(Unit) {
        coordinator.observeResult<Boolean>(NavigationCoordinator.KEY_LOGOUT_CONFIRMED)
            .collect { confirmed -> if (confirmed == true) viewModel.onLogoutConfirmed() }
    }
    LaunchedEffect(Unit) {
        coordinator.observeResult<Boolean>(NavigationCoordinator.KEY_DELETE_CONFIRMED)
            .collect { confirmed -> if (confirmed == true) viewModel.onDeleteConfirmed() }
    }

    val navAction = object : EditProfileScreenNavAction {
        override fun navigateBack() {
            coordinator.goBack()
        }

        override fun navigateToAuth() = coordinator.navigateAndClearBackStack(CompleteKey)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                EditProfileEffect.NavigateToAuth ->
                    coordinator.navigateAndClearBackStack(CompleteKey)

                EditProfileEffect.NavigateToLogin ->
                    coordinator.navigate(LoginKey())

                EditProfileEffect.NavigateToConfirmDelete ->
                    coordinator.navigate(LoginKey(confirmDelete = true))

                EditProfileEffect.OpenPhotoSheet ->
                    coordinator.showBottomSheet(ChangePhotoSheetKey)

                is EditProfileEffect.OpenFieldSheet ->
                    coordinator.showBottomSheet(
                        EditFieldSheetKey(field = effect.field, initialValue = effect.initialValue),
                    )

                EditProfileEffect.OpenLogoutDialog ->
                    coordinator.showDialog(LogoutDialogKey)

                EditProfileEffect.OpenDeleteDialog ->
                    coordinator.showDialog(DeleteAccountDialogKey)
            }
        }
    }

    EditProfileScreen(
        state = state.value,
        intent = viewModel,
        navAction = navAction,
        paddingValues = paddingValues,
        snackbarHostState = snackbarHostState,
    )
}
