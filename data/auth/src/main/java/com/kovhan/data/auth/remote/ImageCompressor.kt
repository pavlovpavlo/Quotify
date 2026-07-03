package com.kovhan.data.auth.remote

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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

        val scaled = source.scaledDown(MAX_DIMENSION)
        val target = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
        target.outputStream().use { out ->
            scaled.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, out)
        }
        if (scaled !== source) scaled.recycle()
        source.recycle()
        return target
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
