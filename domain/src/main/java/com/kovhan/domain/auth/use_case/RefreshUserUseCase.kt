package com.kovhan.domain.auth.use_case

import com.kovhan.domain.auth.AuthRepository
import com.kovhan.domain.auth.AuthResult
import com.kovhan.domain.auth.AuthUser
import javax.inject.Inject

class RefreshUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): AuthResult<AuthUser> = authRepository.reloadUser()
}
