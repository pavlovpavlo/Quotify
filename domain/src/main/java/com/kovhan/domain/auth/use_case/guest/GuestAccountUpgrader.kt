package com.kovhan.domain.auth.use_case.guest

import com.kovhan.core.models.AuthUser
import com.kovhan.core.models.Outcome
import com.kovhan.domain.auth.AuthRepository
import com.kovhan.domain.auth.UserRepository
import com.kovhan.domain.auth.model.AuthError
import com.kovhan.domain.auth.model.AuthResult
import com.kovhan.domain.library.GuestLibraryMerger
import com.kovhan.domain.library.sync.LibrarySynchronizer
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Central orchestrator for authenticating and, when the current user is an anonymous guest,
 * carrying the guest's library and (for in-place upgrades) profile into the target account.
 *
 * Two shapes:
 *  - **Link in place** (`linkWithCredential`): the Firebase uid is preserved, so no library
 *    merge is needed — the local Room data already belongs to the upgraded account. Only a
 *    normal sync + refresh runs.
 *  - **Sign in + merge**: the target is an existing/different account (uid changes). A snapshot
 *    of the guest library is taken BEFORE the switch, then target data is loaded, the snapshot is
 *    merged in, pending changes are synced and a final refresh runs. The existing account's
 *    profile takes priority and is never overwritten by guest profile data.
 */
@Singleton
class GuestAccountUpgrader @Inject constructor(
    private val authRepository: AuthRepository,
    private val merger: GuestLibraryMerger,
    private val synchronizer: LibrarySynchronizer,
    private val userRepository: UserRepository,
) {

    suspend fun continueAsGuest(): AuthResult<AuthUser> = authRepository.signInAnonymously()

    suspend fun signInWithEmail(email: String, password: String): AuthResult<AuthUser> {
        val trimmed = email.trim()
        return if (isGuest()) {
            signInAndMerge { authRepository.signIn(trimmed, password) }
        } else {
            authRepository.signIn(trimmed, password).finalizeSimpleOnSuccess()
        }
    }

    suspend fun registerWithEmail(
        email: String,
        password: String,
        username: String,
        displayName: String?,
    ): AuthResult<AuthUser> {
        val trimmed = email.trim()
        if (isGuest()) {
            val linked = authRepository.linkEmailPassword(trimmed, password, displayName)
            return when (linked) {
                is Outcome.Success -> {
                    persistUsername(username)
                    finalizeSimple()
                    linked
                }

                is Outcome.Failure ->
                    if (linked.error == AuthError.CredentialAlreadyInUse) {
                        // Email belongs to an existing account: sign into it and merge the guest library.
                        signInAndMerge { authRepository.signIn(trimmed, password) }
                    } else {
                        linked
                    }
            }
        }
        val result = authRepository.signUp(trimmed, password, displayName)
        if (result is Outcome.Success) {
            persistUsername(username)
            finalizeSimple()
        }
        return result
    }

    suspend fun authWithGoogle(idToken: String): AuthResult<AuthUser> {
        if (isGuest()) {
            val linked = authRepository.linkGoogle(idToken)
            return when (linked) {
                is Outcome.Success -> {
                    finalizeSimple()
                    linked
                }

                is Outcome.Failure ->
                    if (linked.error == AuthError.CredentialAlreadyInUse) {
                        signInAndMerge { authRepository.signInWithGoogle(idToken) }
                    } else {
                        linked
                    }
            }
        }
        return authRepository.signInWithGoogle(idToken).finalizeSimpleOnSuccess()
    }

    private fun isGuest(): Boolean = authRepository.currentUser()?.isAnonymous == true

    private suspend fun signInAndMerge(
        signIn: suspend () -> AuthResult<AuthUser>,
    ): AuthResult<AuthUser> {
        val snapshot = runCatching { merger.snapshot() }.getOrNull()
        val result = signIn()
        if (result is Outcome.Success && snapshot != null) {
            runCatching { merger.clearPending() }
            runCatching { synchronizer.refreshFromRemote() }
            runCatching { merger.merge(snapshot) }
            runCatching { synchronizer.syncPendingChanges() }
            runCatching { synchronizer.refreshFromRemote() }
        }
        return result
    }

    private suspend fun persistUsername(username: String) {
        val clean = username.trim().replace("@", "")
        if (clean.isNotEmpty()) runCatching { userRepository.setUsername(clean) }
    }

    private suspend fun AuthResult<AuthUser>.finalizeSimpleOnSuccess(): AuthResult<AuthUser> {
        if (this is Outcome.Success) finalizeSimple()
        return this
    }

    private suspend fun finalizeSimple() {
        runCatching { synchronizer.syncPendingChanges() }
        runCatching { synchronizer.refreshFromRemote() }
    }
}
