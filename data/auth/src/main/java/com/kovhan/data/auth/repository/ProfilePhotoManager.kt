package com.kovhan.data.auth.repository

import com.kovhan.data.auth.remote.AuthRemoteDataSource
import com.kovhan.data.auth.source.PhotoUploader
import com.kovhan.data.auth.source.RemoteUserProfileSource
import com.kovhan.data.auth.source.UserCache
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfilePhotoManager @Inject constructor(
    private val cache: UserCache,
    private val profile: RemoteUserProfileSource,
    private val uploader: PhotoUploader,
    private val remote: AuthRemoteDataSource,
) {

    private val uid: String? get() = remote.currentUserId()

    suspend fun setPhoto(sourceUri: String): Boolean {
        val id = uid ?: return false
        val previous = runCatching { profile.fetchProfile(id) }.getOrNull()
        cache.setPhotoUrl(sourceUri)
        return try {
            val uploaded = uploader.upload(sourceUri)
            profile.setPhoto(id, url = uploaded.secureUrl, publicId = uploaded.publicId)
            cache.setPhotoUrl(uploaded.secureUrl)
            previous?.photoPublicId
                ?.takeIf { it != uploaded.publicId }
                ?.let { old -> runCatching { uploader.delete(old) } }
            true
        } catch (t: Throwable) {
            cache.setPhotoUrl(previous?.photoUrl)
            false
        }
    }

    suspend fun clearPhoto() {
        val id = uid ?: return
        profile.fetchProfile(id)?.photoPublicId?.let { old ->
            runCatching { uploader.delete(old) }
        }
        profile.setPhoto(id, url = null, publicId = null)
        cache.setPhotoUrl(remote.currentPhotoUrl())
    }
}
