package com.kovhan.domain.auth

import com.kovhan.core.models.AuthUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeUser(): Flow<AuthUser?>

    suspend fun cacheUser(user: AuthUser)

    suspend fun setDisplayName(displayName: String)

    suspend fun setUsername(username: String)

    suspend fun setEmail(email: String)

    suspend fun setCustomPhoto(sourceUri: String): Boolean

    suspend fun clearCustomPhoto()

    /** Deletes the user's remote profile document and Cloudinary photo (used on account deletion). */
    suspend fun purgeRemoteProfile()

    suspend fun clear()
}
