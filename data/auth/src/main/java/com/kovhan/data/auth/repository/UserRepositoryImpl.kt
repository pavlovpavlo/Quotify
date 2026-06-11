package com.kovhan.data.auth.repository

import com.google.firebase.auth.FirebaseAuth
import com.kovhan.data.auth.local.UserPreferencesDataStore
import com.kovhan.data.auth.remote.CloudinaryPhotoUploader
import com.kovhan.data.auth.remote.FirestoreUserRepository
import com.kovhan.domain.auth.AuthUser
import com.kovhan.domain.auth.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val cache: UserPreferencesDataStore,
    private val profile: FirestoreUserRepository,
    private val photoUploader: CloudinaryPhotoUploader,
    private val auth: FirebaseAuth,
) : UserRepository {

    private val uid: String? get() = auth.currentUser?.uid

    override fun observeUser(): Flow<AuthUser?> = cache.observe()

    override suspend fun cacheUser(user: AuthUser) = cache.cache(user)

    override suspend fun setDisplayName(displayName: String) {
        cache.setDisplayName(displayName)
    }

    override suspend fun setUsername(username: String) {
        uid?.let { profile.setUsername(it, username) }
        cache.setUsername(username)
    }

    override suspend fun setEmail(email: String) {
        cache.setEmail(email)
    }

    override suspend fun setCustomPhoto(sourceUri: String): Boolean {
        val id = uid ?: return false
        val previous = runCatching { profile.fetchProfile(id) }.getOrNull()
        cache.setPhotoUrl(sourceUri)
        return try {
            val uploaded = photoUploader.upload(sourceUri)
            profile.setPhoto(id, url = uploaded.secureUrl, publicId = uploaded.publicId)
            cache.setPhotoUrl(uploaded.secureUrl)
            previous?.photoPublicId
                ?.takeIf { it != uploaded.publicId }
                ?.let { old -> runCatching { photoUploader.delete(old) } }
            true
        } catch (t: Throwable) {
            cache.setPhotoUrl(previous?.photoUrl)
            false
        }
    }

    override suspend fun clearCustomPhoto() {
        val id = uid ?: return
        profile.fetchProfile(id)?.photoPublicId?.let { old ->
            runCatching { photoUploader.delete(old) }
        }
        profile.setPhoto(id, url = null, publicId = null)
        cache.setPhotoUrl(auth.currentUser?.photoUrl?.toString())
    }

    override suspend fun clear() = cache.clear()
}
