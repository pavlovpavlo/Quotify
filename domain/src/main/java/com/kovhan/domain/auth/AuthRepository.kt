package com.kovhan.domain.auth

interface AuthRepository {
    suspend fun signIn(
        email: String,
        password: String,
    ): AuthResult<AuthUser>

    suspend fun signUp(
        email: String,
        password: String,
        displayName: String?,
    ): AuthResult<AuthUser>

    suspend fun sendPasswordReset(email: String): AuthResult<Unit>

    suspend fun signInWithGoogle(idToken: String): AuthResult<AuthUser>

    suspend fun updateDisplayName(displayName: String): AuthResult<AuthUser>

    fun currentUser(): AuthUser?

    fun signOut()
}
