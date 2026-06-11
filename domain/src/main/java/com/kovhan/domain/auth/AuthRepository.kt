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

    suspend fun updateEmail(newEmail: String): AuthResult<Unit>

    suspend fun reloadUser(): AuthResult<AuthUser>

    suspend fun changePassword(currentPassword: String, newPassword: String): AuthResult<Unit>

    suspend fun deleteAccount(): AuthResult<Unit>

    fun currentUser(): AuthUser?

    fun signOut()
}
