package com.kovhan.feature.main.presentation.edit_profile.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

internal fun hasCameraPermission(context: Context): Boolean =
    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
        PackageManager.PERMISSION_GRANTED

/**
 * Destination for a full-size camera capture. The thumbnail returned by
 * `TakePicturePreview` comes back mirrored from some OEM camera apps, so the
 * capture is written to a shared file instead.
 */
internal fun Context.createCameraPhotoUri(): Uri {
    val directory = File(cacheDir, CAMERA_DIRECTORY).apply { mkdirs() }
    val file = File(directory, "camera_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
}

private const val CAMERA_DIRECTORY = "images"
