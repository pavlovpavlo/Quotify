package com.kovhan.domain.auth.use_case

import com.kovhan.domain.auth.AuthRepository
import com.kovhan.domain.auth.model.AuthResult
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): AuthResult<Unit> = authRepository.deleteAccount()
}
