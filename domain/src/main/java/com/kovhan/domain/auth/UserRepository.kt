package com.kovhan.domain.auth

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeUser(): Flow<AuthUser?>

    suspend fun cacheUser(user: AuthUser)

    suspend fun clear()
}
