package com.kovhan.domain.auth.use_case

import com.kovhan.domain.auth.AuthRepository
import com.kovhan.domain.auth.UserRepository
import com.kovhan.domain.library.sync.LibrarySynchronizer
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val synchronizer: LibrarySynchronizer,
) {
    suspend operator fun invoke() {
        authRepository.signOut()
        userRepository.clear()
        runCatching { synchronizer.clearLocal() }
    }
}
