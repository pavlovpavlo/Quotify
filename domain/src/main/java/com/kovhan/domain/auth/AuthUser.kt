package com.kovhan.domain.auth

data class AuthUser(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val isEmailVerified: Boolean,
    val photoUrl: String? = null,
    val username: String? = null,
    val isGoogleAccount: Boolean = false,
)
