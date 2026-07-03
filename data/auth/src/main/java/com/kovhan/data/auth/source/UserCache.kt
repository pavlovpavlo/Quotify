package com.kovhan.data.auth.source

import com.kovhan.core.models.AuthUser
import kotlinx.coroutines.flow.Flow

interface UserCache {
    fun observe(): Flow<AuthUser?>

    suspend fun cache(user: AuthUser)

    suspend fun setDisplayName(displayName: String)

    suspend fun setUsername(username: String)

    suspend fun setEmail(email: String)

    suspend fun setPhotoUrl(url: String?)

    suspend fun clear()
}
