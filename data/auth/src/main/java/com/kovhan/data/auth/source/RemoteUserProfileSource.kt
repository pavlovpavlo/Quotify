package com.kovhan.data.auth.source

data class RemoteProfile(
    val username: String? = null,
    val photoUrl: String? = null,
    val photoPublicId: String? = null,
)

interface RemoteUserProfileSource {
    suspend fun fetchProfile(uid: String): RemoteProfile?

    suspend fun setUsername(uid: String, username: String)

    suspend fun setPhoto(uid: String, url: String?, publicId: String?)

    suspend fun deleteProfile(uid: String)
}
