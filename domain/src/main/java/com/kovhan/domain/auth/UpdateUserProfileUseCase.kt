package com.kovhan.domain.auth

import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(displayName: String): AuthResult<AuthUser> =
        authRepository.updateDisplayName(displayName.trim())
}
