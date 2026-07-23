package com.kovhan.feature.addquote.presentation.addquote.component.scan

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Rect
import android.net.Uri
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

/** Decodes [uri] into a software bitmap (EXIF orientation applied by ImageDecoder). */
internal fun loadBitmap(context: Context, uri: Uri): Bitmap? = runCatching {
    val source = ImageDecoder.createSource(context.contentResolver, uri)
    ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
        decoder.isMutableRequired = false
    }
}.onFailure { Timber.e(it, "loadBitmap failed") }.getOrNull()

/** Crops [source] to [rect] (bitmap pixel space) and writes a JPEG into the cache, returning its Uri. */
internal fun cropToCache(context: Context, source: Bitmap, rect: Rect): Uri? = runCatching {
    val x = rect.left.coerceIn(0, source.width - 1)
    val y = rect.top.coerceIn(0, source.height - 1)
    val w = rect.width().coerceIn(1, source.width - x)
    val h = rect.height().coerceIn(1, source.height - y)
    val cropped = Bitmap.createBitmap(source, x, y, w, h)
    val file = File(context.cacheDir, "scan_crop_${System.currentTimeMillis()}.jpg")
    FileOutputStream(file).use { cropped.compress(Bitmap.CompressFormat.JPEG, 92, it) }
    Uri.fromFile(file)
}.onFailure { Timber.e(it, "cropToCache failed") }.getOrNull()
