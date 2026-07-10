package com.kovhan.domain.auth.use_case.guest

import com.kovhan.core.models.AuthUser
import com.kovhan.domain.auth.model.AuthResult
import javax.inject.Inject

/**
 * Signs in with email/password. If the current user is an anonymous guest, the guest library is
 * snapshotted and merged into the target account after sign-in.
 */
class GuestAwareSignInUseCase @Inject constructor(
    private val upgrader: GuestAccountUpgrader,
) {
    suspend operator fun invoke(email: String, password: String): AuthResult<AuthUser> =
        upgrader.signInWithEmail(email, password)
}
