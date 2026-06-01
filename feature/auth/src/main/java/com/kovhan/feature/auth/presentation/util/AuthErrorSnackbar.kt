package com.kovhan.feature.auth.presentation.util

import com.kovhan.core.ui.snackbar.SnackbarMessage
import com.kovhan.domain.auth.AuthError
import com.kovhan.domain.auth.ValidationError
import com.kovhan.design.systems.R

internal fun AuthError.toSnackbar(): SnackbarMessage = SnackbarMessage.error(stringRes())

internal fun ValidationError.toSnackbar(): SnackbarMessage = SnackbarMessage.error(
    when (this) {
        ValidationError.EmptyName -> R.string.auth_validation_name
        ValidationError.InvalidEmail -> R.string.auth_validation_email
        ValidationError.ShortPassword -> R.string.auth_validation_password
    },
)

private fun AuthError.stringRes(): Int = when (this) {
    AuthError.InvalidCredentials -> R.string.auth_error_invalid_credentials
    AuthError.InvalidEmail -> R.string.auth_error_invalid_email
    AuthError.EmailAlreadyInUse -> R.string.auth_error_email_in_use
    AuthError.WeakPassword -> R.string.auth_error_weak_password
    AuthError.UserNotFound -> R.string.auth_error_user_not_found
    AuthError.UserDisabled -> R.string.auth_error_user_disabled
    AuthError.TooManyRequests -> R.string.auth_error_too_many_requests
    AuthError.Network -> R.string.auth_error_network
    AuthError.GoogleSignInCancelled -> R.string.auth_error_google_cancelled
    AuthError.GoogleSignInFailed -> R.string.auth_error_google_failed
    is AuthError.Unknown -> R.string.auth_error_generic
}
