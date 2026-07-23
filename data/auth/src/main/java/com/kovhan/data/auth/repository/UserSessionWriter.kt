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
        val username = (remote?.username ?: user.username)
            ?.takeUnless { it.isBlank() }
            ?: seedGoogleUsername(user)
        val merged = user.copy(
            username = username,
            photoUrl = remote?.photoUrl ?: user.photoUrl,
        )
        cache.cache(merged)
        return merged
    }

    /**
     * Email-prefix username fallback is allowed only for Google accounts that don't yet have a
     * username, and only seeded once into the Firestore profile. Email/password and anonymous
     * users never get an email-derived username.
     */
    private suspend fun seedGoogleUsername(user: AuthUser): String? {
        if (!user.isGoogleAccount) return null
        val seeded = user.email?.substringBefore("@")?.trim().orEmpty()
        if (seeded.isEmpty()) return null
        runCatching { profile.setUsername(user.uid, seeded) }
        return seeded
    }
}
