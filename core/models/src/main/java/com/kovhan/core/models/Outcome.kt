package com.kovhan.core.models

sealed interface Outcome<out T, out E> {
    data class Success<out T>(val data: T) : Outcome<T, Nothing>

    data class Failure<out E>(val error: E) : Outcome<Nothing, E>
}

val Outcome<*, *>.isSuccess: Boolean get() = this is Outcome.Success

inline fun <T, E> Outcome<T, E>.onSuccess(action: (T) -> Unit): Outcome<T, E> {
    if (this is Outcome.Success) action(data)
    return this
}

inline fun <T, E> Outcome<T, E>.onFailure(action: (E) -> Unit): Outcome<T, E> {
    if (this is Outcome.Failure) action(error)
    return this
}
