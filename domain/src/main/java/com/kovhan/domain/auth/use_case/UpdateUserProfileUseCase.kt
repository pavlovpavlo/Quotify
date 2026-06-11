package com.kovhan.domain.auth.use_case

import com.kovhan.domain.auth.AuthRepository
import com.kovhan.domain.auth.AuthResult
import com.kovhan.domain.auth.AuthUser
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(displayName: String): AuthResult<AuthUser> =
        authRepository.updateDisplayName(displayName.trim())
}
