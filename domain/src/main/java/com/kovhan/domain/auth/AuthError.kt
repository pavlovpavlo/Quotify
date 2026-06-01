package com.kovhan.domain.auth

sealed class AuthError {
    data object InvalidCredentials : AuthError()

    data object InvalidEmail : AuthError()

    data object EmailAlreadyInUse : AuthError()

    data object WeakPassword : AuthError()

    data object UserNotFound : AuthError()

    data object UserDisabled : AuthError()

    data object TooManyRequests : AuthError()

    data object Network : AuthError()

    data object GoogleSignInCancelled : AuthError()

    data object GoogleSignInFailed : AuthError()

    data class Unknown(val message: String?) : AuthError()
}
