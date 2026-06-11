package com.kovhan.domain.auth.use_case

import com.kovhan.domain.auth.AuthRepository
import com.kovhan.domain.auth.AuthResult
import javax.inject.Inject

class SendPasswordResetUseCase
    @Inject
    constructor(
        private val repository: AuthRepository,
    ) {
        suspend operator fun invoke(email: String): AuthResult<Unit> = repository.sendPasswordReset(email.trim())
    }
