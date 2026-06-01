package com.kovhan.domain.auth

import javax.inject.Inject

class SendPasswordResetUseCase
    @Inject
    constructor(
        private val repository: AuthRepository,
    ) {
        suspend operator fun invoke(email: String): AuthResult<Unit> = repository.sendPasswordReset(email.trim())
    }
