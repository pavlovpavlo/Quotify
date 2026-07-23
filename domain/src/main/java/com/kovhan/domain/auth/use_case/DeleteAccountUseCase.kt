package com.kovhan.domain.auth.use_case

import com.kovhan.core.models.Outcome
import com.kovhan.domain.auth.AuthRepository
import com.kovhan.domain.auth.UserRepository
import com.kovhan.domain.auth.model.AuthResult
import com.kovhan.domain.library.sync.LibrarySynchronizer
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val synchronizer: LibrarySynchronizer,
) {
    suspend operator fun invoke(): AuthResult<Unit> {
        // Purge remote data while still authenticated — Firestore rules require auth.uid == uid,
        // and after the auth account is gone the orphaned docs/photo can no longer be reached.
        runCatching { synchronizer.purgeRemote() }
        runCatching { userRepository.purgeRemoteProfile() }
        val result = authRepository.deleteAccount()
        if (result is Outcome.Success) runCatching { synchronizer.clearLocal() }
        return result
    }
}
