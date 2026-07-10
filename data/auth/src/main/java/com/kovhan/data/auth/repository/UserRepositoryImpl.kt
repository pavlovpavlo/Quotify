package com.kovhan.data.auth.repository

import com.kovhan.core.models.AuthUser
import com.kovhan.data.auth.remote.AuthRemoteDataSource
import com.kovhan.data.auth.source.RemoteUserProfileSource
import com.kovhan.data.auth.source.UserCache
import com.kovhan.domain.auth.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val cache: UserCache,
    private val profile: RemoteUserProfileSource,
    private val photoManager: ProfilePhotoManager,
    private val remote: AuthRemoteDataSource,
) : UserRepository {

    private val uid: String? get() = remote.currentUserId()

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

    override suspend fun setCustomPhoto(sourceUri: String): Boolean = photoManager.setPhoto(sourceUri)

    override suspend fun clearCustomPhoto() = photoManager.clearPhoto()

    override suspend fun purgeRemoteProfile() {
        val id = uid ?: return
        runCatching { photoManager.deletePhoto() }
        runCatching { profile.deleteProfile(id) }
    }

    override suspend fun clear() = cache.clear()
}
