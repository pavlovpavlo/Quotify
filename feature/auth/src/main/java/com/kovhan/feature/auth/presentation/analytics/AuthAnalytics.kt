package com.kovhan.feature.auth.presentation.analytics

import com.kovhan.core.analytics.AuthEntry
import com.kovhan.core.navigation.models.AuthEntryPoint
import com.kovhan.domain.auth.model.AuthError

internal fun AuthEntryPoint.toAnalytics(): AuthEntry = when (this) {
    AuthEntryPoint.FIRST_LAUNCH -> AuthEntry.FIRST_LAUNCH
    AuthEntryPoint.SETTINGS -> AuthEntry.SETTINGS
}

internal fun AuthError.toFailureName(): String = when (this) {
    AuthError.InvalidCredentials -> "invalid_credentials"
    AuthError.InvalidEmail -> "invalid_email"
    AuthError.EmailAlreadyInUse -> "email_already_in_use"
    AuthError.CredentialAlreadyInUse -> "credential_already_in_use"
    AuthError.WeakPassword -> "weak_password"
    AuthError.UserNotFound -> "user_not_found"
    AuthError.UserDisabled -> "user_disabled"
    AuthError.TooManyRequests -> "too_many_requests"
    AuthError.RecentLoginRequired -> "recent_login_required"
    AuthError.Network -> "network_error"
    AuthError.GoogleSignInCancelled -> "google_sign_in_cancelled"
    AuthError.GoogleSignInFailed -> "google_sign_in_failed"
    is AuthError.Unknown -> "unknown"
}
