package com.kovhan.feature.main.presentation.edit_profile.navigation

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.ChangePhotoSheetKey
import com.kovhan.core.navigation.CompleteKey
import com.kovhan.core.navigation.DeleteAccountDialogKey
import com.kovhan.core.navigation.EditField
import com.kovhan.core.navigation.EditFieldSheetKey
import com.kovhan.core.navigation.LoginKey
import com.kovhan.core.navigation.models.AuthEntryPoint
import com.kovhan.core.navigation.LogoutDialogKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.PhotoAction
import com.kovhan.feature.main.presentation.edit_profile.EditProfileScreen
import com.kovhan.feature.main.presentation.edit_profile.EditProfileViewModel
import com.kovhan.feature.main.presentation.edit_profile.mvi.EditProfileEffect
import com.kovhan.feature.main.presentation.edit_profile.util.createCameraPhotoUri
import com.kovhan.feature.main.presentation.edit_profile.util.hasCameraPermission

@Composable
internal fun EditProfileEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<EditProfileViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        uri?.let { viewModel.onPhotoPicked(it.toString()) }
    }

    val pendingCameraUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture(),
    ) { saved: Boolean ->
        val uri = pendingCameraUri.value
        pendingCameraUri.value = null
        if (saved && uri != null) viewModel.onPhotoPicked(uri.toString())
    }

    val launchCamera: () -> Unit = {
        val uri = context.createCameraPhotoUri()
        pendingCameraUri.value = uri
        cameraLauncher.launch(uri)
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) launchCamera() else viewModel.onCameraPermissionDenied()
    }

    LaunchedEffect(Unit) {
        coordinator.observeResult<PhotoAction>(NavigationCoordinator.KEY_PHOTO_ACTION)
            .collect { action ->
                when (action) {
                    PhotoAction.TAKE ->
                        if (hasCameraPermission(context)) launchCamera()
                        else cameraPermissionLauncher.launch(Manifest.permission.CAMERA)

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
                    coordinator.navigate(LoginKey(entryPoint = AuthEntryPoint.SETTINGS))

                EditProfileEffect.NavigateToConfirmDelete ->
                    coordinator.navigate(LoginKey(confirmDelete = true, entryPoint = AuthEntryPoint.SETTINGS))

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
    )
}
