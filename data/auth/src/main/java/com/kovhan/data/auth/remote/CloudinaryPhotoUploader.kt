package com.kovhan.data.auth.remote

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

data class UploadedPhoto(
    val secureUrl: String,
    val publicId: String,
)

@Singleton
class CloudinaryPhotoUploader @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    init {
        runCatching { MediaManager.init(context, mapOf("cloud_name" to CLOUD_NAME)) }
    }

    suspend fun upload(sourceUri: String): UploadedPhoto {
        val file = withContext(Dispatchers.IO) { compress(Uri.parse(sourceUri)) }
            ?: throw IllegalStateException("Could not read the picked image")

        return suspendCancellableCoroutine { cont ->
            MediaManager.get()
                .upload(Uri.fromFile(file))
                .unsigned(UPLOAD_PRESET)
                .option("folder", UPLOAD_FOLDER)
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String?) = Unit

                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) = Unit

                    override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                        runCatching { file.delete() }
                        val url = resultData?.get("secure_url") as? String
                        val publicId = resultData?.get("public_id") as? String
                        if (url != null && publicId != null) {
                            cont.resume(UploadedPhoto(secureUrl = url, publicId = publicId))
                        } else {
                            cont.resumeWithException(IllegalStateException("Cloudinary returned no secure_url"))
                        }
                    }

                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        runCatching { file.delete() }
                        cont.resumeWithException(
                            IllegalStateException(error?.description ?: "Cloudinary upload failed"),
                        )
                    }

                    override fun onReschedule(requestId: String?, error: ErrorInfo?) = Unit
                })
                .dispatch()
        }
    }

    suspend fun delete(publicId: String) {
        withContext(Dispatchers.IO) { destroy(publicId) }
    }

    private fun destroy(publicId: String) {
        val timestamp = (System.currentTimeMillis() / 1000).toString()
        val signature = sha1("invalidate=true&public_id=$publicId&timestamp=$timestamp$API_SECRET")
        val body = listOf(
            "public_id" to publicId,
            "timestamp" to timestamp,
            "api_key" to API_KEY,
            "signature" to signature,
            "invalidate" to "true",
        ).joinToString("&") { (k, v) -> "$k=${URLEncoder.encode(v, "UTF-8")}" }

        val connection = (URL("$API_BASE/$CLOUD_NAME/image/destroy").openConnection() as HttpURLConnection)
        connection.apply {
            requestMethod = "POST"
            doOutput = true
            connectTimeout = 15_000
            readTimeout = 15_000
            setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        }
        try {
            connection.outputStream.use { it.write(body.toByteArray()) }
            connection.responseCode
        } finally {
            connection.disconnect()
        }
    }

    private fun sha1(value: String): String =
        MessageDigest.getInstance("SHA-1")
            .digest(value.toByteArray())
            .joinToString("") { "%02x".format(it) }

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
        // API secret ships in the APK and is extractable â€” move signing to a backend and rotate later.
        const val CLOUD_NAME = "dx4nrixfo"
        const val API_KEY = "914151881964781"
        const val API_SECRET = "xAJ2UPmTkk9lOGTOUTfuJdxCWqI"
        const val UPLOAD_PRESET = "ml_default"
        const val UPLOAD_FOLDER = "user_photos"
        const val API_BASE = "https://api.cloudinary.com/v1_1"
        const val MAX_DIMENSION = 800
        const val JPEG_QUALITY = 80
    }
}
