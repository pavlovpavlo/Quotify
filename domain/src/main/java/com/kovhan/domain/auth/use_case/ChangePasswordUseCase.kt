package com.kovhan.domain.auth.use_case

import com.kovhan.domain.auth.AuthRepository
import com.kovhan.domain.auth.model.AuthResult
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(currentPassword: String, newPassword: String): AuthResult<Unit> =
        authRepository.changePassword(currentPassword, newPassword)
}
