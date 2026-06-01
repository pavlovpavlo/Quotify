package com.kovhan.domain.auth

sealed interface AuthResult<out T> {
    data class Success<out T>(val data: T) : AuthResult<T>

    data class Failure(val error: AuthError) : AuthResult<Nothing>
}

val AuthResult<*>.isSuccess: Boolean get() = this is AuthResult.Success

inline fun <T> AuthResult<T>.onSuccess(action: (T) -> Unit): AuthResult<T> {
    if (this is AuthResult.Success) action(data)
    return this
}

inline fun <T> AuthResult<T>.onFailure(action: (AuthError) -> Unit): AuthResult<T> {
    if (this is AuthResult.Failure) action(error)
    return this
}
