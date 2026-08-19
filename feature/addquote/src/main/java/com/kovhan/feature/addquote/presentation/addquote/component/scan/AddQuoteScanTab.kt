package com.kovhan.feature.addquote.presentation.addquote.component.scan

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.domain.ai.AiDenialReason
import com.kovhan.feature.addquote.presentation.addquote.mvi.ScanMode
import kotlinx.coroutines.launch

@Composable
internal fun AddQuoteScanTab(
    mode: ScanMode,
    pickedImage: Uri?,
    lines: List<String>,
    noTextFound: Boolean,
    offline: Boolean,
    aiDenial: AiDenialReason?,
    onImagePicked: (Uri) -> Unit,
    onCropConfirmed: (Uri) -> Unit,
    onCropCancelled: () -> Unit,
    onRetake: () -> Unit,
    onRetry: () -> Unit,
    onUpgrade: () -> Unit,
    onPermissionResult: (Boolean) -> Unit,
    onProceed: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val imageCapture = remember { ImageCapture.Builder().build() }
    var hasCameraPermission by remember { mutableStateOf(hasCameraPermission(context)) }

    val view = LocalView.current
    val configuration = LocalConfiguration.current
    LaunchedEffect(configuration) {
        view.display?.rotation?.let { imageCapture.targetRotation = it }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        hasCameraPermission = granted
        onPermissionResult(granted)
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia(),
    ) { uri -> uri?.let(onImagePicked) }

    var scanned by remember(lines) {
        val text = lines.joinToString(separator = "\n")
        mutableStateOf(TextFieldValue(text, TextRange(text.length)))
    }

    val selectedOrAll: () -> String = {
        val selection = scanned.selection
        if (selection.collapsed) {
            scanned.text
        } else {
            scanned.text.substring(selection.min, selection.max)
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        if (mode == ScanMode.CROP && pickedImage != null) {
            ScanCropStage(
                imageUri = pickedImage,
                onCancel = onCropCancelled,
                onConfirm = onCropConfirmed,
                modifier = Modifier.fillMaxSize(),
            )
            return@Column
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            when (mode) {
                ScanMode.LIVE -> ScanStage(
                    hasCameraPermission = hasCameraPermission,
                    imageCapture = imageCapture,
                    noTextFound = noTextFound,
                    offline = offline,
                    aiDenial = aiDenial,
                    onRequestPermission = {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    onRetry = onRetry,
                    onUpgrade = onUpgrade,
                )

                ScanMode.SCANNING -> ScanProcessingStage(imageUri = pickedImage)

                ScanMode.SELECT -> ScanTextSelector(
                    value = scanned,
                    onValueChange = { scanned = it },
                )

                ScanMode.CROP -> Unit
            }
        }

        ScanDock(
            mode = mode,
            canProceed = scanned.text.isNotBlank(),
            onGallery = {
                galleryLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                )
            },
            onShutter = {
                if (hasCameraPermission) {
                    scope.launch {
                        imageCapture.captureToCache(context)?.let(onImagePicked)
                    }
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            onRetake = onRetake,
            onProceed = { onProceed(selectedOrAll()) },
        )
    }
}
