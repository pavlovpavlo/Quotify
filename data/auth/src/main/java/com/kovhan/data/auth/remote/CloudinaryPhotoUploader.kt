package com.kovhan.data.auth.remote

import com.kovhan.data.auth.source.PhotoUploader
import com.kovhan.data.auth.source.UploadedPhoto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CloudinaryPhotoUploader @Inject constructor(
    private val compressor: ImageCompressor,
    private val client: CloudinaryClient,
) : PhotoUploader {

    override suspend fun upload(sourceUri: String): UploadedPhoto {
        val file = compressor.compress(sourceUri)
            ?: throw IllegalStateException("Could not read the picked image")
        return client.uploadFile(file)
    }

    override suspend fun delete(publicId: String) {
        client.destroy(publicId)
    }
}
