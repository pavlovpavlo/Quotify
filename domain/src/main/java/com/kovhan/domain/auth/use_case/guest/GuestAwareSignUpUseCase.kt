package com.kovhan.domain.auth.use_case.guest

import com.kovhan.core.models.AuthUser
import com.kovhan.domain.auth.model.AuthResult
import javax.inject.Inject

/**
 * Registers an email/password account. If the current user is an anonymous guest, the account is
 * upgraded in place via credential linking (guest uid, library and profile preserved); the entered
 * [username] is persisted to the Firestore profile. Falls back to sign-in + merge if the email is
 * already registered.
 */
class GuestAwareSignUpUseCase @Inject constructor(
    private val upgrader: GuestAccountUpgrader,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        username: String,
        displayName: String?,
    ): AuthResult<AuthUser> = upgrader.registerWithEmail(email, password, username, displayName)
}
