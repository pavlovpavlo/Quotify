package com.kovhan.feature.main.presentation.edit_profile

import com.kovhan.core.ui.snackbar.SnackbarMessage
import com.kovhan.design.systems.R
import com.kovhan.domain.auth.model.AuthError

internal fun AuthError.toSnackbar(): SnackbarMessage = SnackbarMessage.error(
    when (this) {
        AuthError.InvalidCredentials -> R.string.auth_error_invalid_credentials
        AuthError.InvalidEmail -> R.string.auth_error_invalid_email
        AuthError.EmailAlreadyInUse -> R.string.auth_error_email_in_use
        AuthError.WeakPassword -> R.string.auth_error_weak_password
        AuthError.UserNotFound -> R.string.auth_error_user_not_found
        AuthError.UserDisabled -> R.string.auth_error_user_disabled
        AuthError.TooManyRequests -> R.string.auth_error_too_many_requests
        AuthError.RecentLoginRequired -> R.string.auth_error_recent_login_required
        AuthError.Network -> R.string.auth_error_network
        AuthError.GoogleSignInCancelled -> R.string.auth_error_google_cancelled
        AuthError.GoogleSignInFailed -> R.string.auth_error_google_failed
        is AuthError.Unknown -> R.string.auth_error_generic
    },
)
