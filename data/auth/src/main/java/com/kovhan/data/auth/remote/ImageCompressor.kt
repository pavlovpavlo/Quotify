package com.kovhan.data.auth.remote

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageCompressor @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    suspend fun compress(sourceUri: String): File? =
        withContext(Dispatchers.IO) { compress(Uri.parse(sourceUri)) }

    private fun compress(uri: Uri): File? {
        val source = context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it)
        } ?: return null

        val oriented = source.applyExifOrientation(uri)
        val scaled = oriented.scaledDown(MAX_DIMENSION)
        val target = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
        target.outputStream().use { out ->
            scaled.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, out)
        }
        if (scaled !== oriented) scaled.recycle()
        if (oriented !== source) oriented.recycle()
        source.recycle()
        return target
    }

    /**
     * Camera captures carry their rotation (and, on front cameras, a mirror flag)
     * in EXIF instead of the pixels. [BitmapFactory] ignores it, so bake it in here.
     */
    private fun Bitmap.applyExifOrientation(uri: Uri): Bitmap {
        val orientation = runCatching {
            context.contentResolver.openInputStream(uri)?.use { stream ->
                ExifInterface(stream).getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL,
                )
            }
        }.getOrNull() ?: return this

        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.postRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.postRotate(270f)
                matrix.postScale(-1f, 1f)
            }
            else -> return this
        }

        return runCatching {
            Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
        }.getOrDefault(this)
    }

    private fun Bitmap.scaledDown(maxDimension: Int): Bitmap {
        val largest = maxOf(width, height)
        if (largest <= maxDimension) return this
        val ratio = maxDimension.toFloat() / largest
        return Bitmap.createScaledBitmap(
            this,
            (width * ratio).toInt(),
            (height * ratio).toInt(),
            true,
        )
    }

    private companion object {
        const val MAX_DIMENSION = 800
        const val JPEG_QUALITY = 80
    }
}
