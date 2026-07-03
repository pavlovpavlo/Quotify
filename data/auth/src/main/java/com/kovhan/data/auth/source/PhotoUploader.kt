package com.kovhan.data.auth.source

data class UploadedPhoto(
    val secureUrl: String,
    val publicId: String,
)

interface PhotoUploader {
    suspend fun upload(sourceUri: String): UploadedPhoto

    suspend fun delete(publicId: String)
}
