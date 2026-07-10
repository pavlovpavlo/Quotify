package com.kovhan.domain.auth.use_case.guest

import com.kovhan.core.models.AuthUser
import com.kovhan.domain.auth.model.AuthResult
import javax.inject.Inject

/**
 * Signs in with Google. An anonymous guest is upgraded in place via credential linking when
 * possible; if the Google account already exists, it falls back to sign-in + guest-library merge.
 */
class GuestAwareGoogleSignInUseCase @Inject constructor(
    private val upgrader: GuestAccountUpgrader,
) {
    suspend operator fun invoke(idToken: String): AuthResult<AuthUser> =
        upgrader.authWithGoogle(idToken)
}
