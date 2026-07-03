package com.kovhan.data.auth.repository

import com.kovhan.core.models.AuthUser
import com.kovhan.data.auth.source.RemoteUserProfileSource
import com.kovhan.data.auth.source.UserCache
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSessionWriter @Inject constructor(
    private val cache: UserCache,
    private val profile: RemoteUserProfileSource,
) {

    suspend fun persist(user: AuthUser): AuthUser {
        val remote = runCatching { profile.fetchProfile(user.uid) }.getOrNull()
        val merged = user.copy(
            username = remote?.username ?: user.username,
            photoUrl = remote?.photoUrl ?: user.photoUrl,
        )
        cache.cache(merged)
        return merged
    }
}
