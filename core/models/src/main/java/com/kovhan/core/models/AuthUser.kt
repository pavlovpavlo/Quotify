package com.kovhan.core.models

data class AuthUser(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val isEmailVerified: Boolean,
    val photoUrl: String? = null,
    val username: String? = null,
    val isGoogleAccount: Boolean = false,
    val isAnonymous: Boolean = false,
)
