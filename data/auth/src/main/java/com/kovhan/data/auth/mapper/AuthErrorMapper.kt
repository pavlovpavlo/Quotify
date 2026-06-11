package com.kovhan.data.auth.mapper

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.kovhan.domain.auth.AuthError

internal fun Throwable.toAuthError(): AuthError =
    when (this) {
        is FirebaseNetworkException -> AuthError.Network
        is FirebaseTooManyRequestsException -> AuthError.TooManyRequests
        is FirebaseAuthRecentLoginRequiredException -> AuthError.RecentLoginRequired
        is FirebaseAuthWeakPasswordException -> AuthError.WeakPassword
        is FirebaseAuthUserCollisionException -> AuthError.EmailAlreadyInUse
        is FirebaseAuthInvalidUserException ->
            when (errorCode) {
                "ERROR_USER_DISABLED" -> AuthError.UserDisabled
                else -> AuthError.UserNotFound
            }
        is FirebaseAuthInvalidCredentialsException ->
            when (errorCode) {
                "ERROR_INVALID_EMAIL" -> AuthError.InvalidEmail
                else -> AuthError.InvalidCredentials
            }
        else -> fromErrorCode() ?: AuthError.Unknown(message)
    }

// Newer Firebase Auth collapses several cases into ERROR_INVALID_CREDENTIAL; sniff the message.
private fun Throwable.fromErrorCode(): AuthError? {
    val haystack = (message ?: "").uppercase()
    return when {
        "INVALID_LOGIN_CREDENTIALS" in haystack ||
            "INVALID_CREDENTIAL" in haystack ||
            "WRONG_PASSWORD" in haystack -> AuthError.InvalidCredentials
        "EMAIL_ALREADY_IN_USE" in haystack -> AuthError.EmailAlreadyInUse
        "USER_NOT_FOUND" in haystack -> AuthError.UserNotFound
        "INVALID_EMAIL" in haystack -> AuthError.InvalidEmail
        "NETWORK" in haystack -> AuthError.Network
        else -> null
    }
}
