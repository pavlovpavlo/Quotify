package com.kovhan.domain.auth

sealed interface ValidationError {
    data object EmptyName : ValidationError

    data object InvalidEmail : ValidationError

    data object ShortPassword : ValidationError
}
