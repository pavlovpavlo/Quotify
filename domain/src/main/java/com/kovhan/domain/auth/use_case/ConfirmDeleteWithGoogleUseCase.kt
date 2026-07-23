package com.kovhan.domain.auth.use_case

import com.kovhan.core.models.Outcome
import com.kovhan.domain.auth.AuthRepository
import com.kovhan.domain.auth.model.AuthResult
import javax.inject.Inject

/**
 * Re-authenticates the current user with a fresh Google credential and, on success, deletes the
 * account. Used by the sign-in screen shown in confirm-delete mode for Google accounts.
 */
class ConfirmDeleteWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val deleteAccount: DeleteAccountUseCase,
) {
    suspend operator fun invoke(idToken: String): AuthResult<Unit> =
        when (val reauth = repository.reauthenticateWithGoogle(idToken)) {
            is Outcome.Success -> deleteAccount()
            is Outcome.Failure -> reauth
        }
}
