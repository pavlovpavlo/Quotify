package com.kovhan.domain.auth

import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke() {
        authRepository.signOut()
        userRepository.clear()
    }
}
