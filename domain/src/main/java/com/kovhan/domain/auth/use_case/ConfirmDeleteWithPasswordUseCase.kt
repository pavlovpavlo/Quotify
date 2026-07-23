package com.kovhan.domain.auth.use_case

import com.kovhan.core.models.Outcome
import com.kovhan.domain.auth.AuthRepository
import com.kovhan.domain.auth.model.AuthResult
import javax.inject.Inject

/**
 * Re-authenticates the current user with their email/password and, on success, deletes the
 * account. Used by the sign-in screen shown in confirm-delete mode when Firebase requires a
 * recent login before deletion.
 */
class ConfirmDeleteWithPasswordUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val deleteAccount: DeleteAccountUseCase,
) {
    suspend operator fun invoke(email: String, password: String): AuthResult<Unit> =
        when (val reauth = repository.reauthenticateWithPassword(email.trim(), password)) {
            is Outcome.Success -> deleteAccount()
            is Outcome.Failure -> reauth
        }
}
