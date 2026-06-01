package com.kovhan.domain.auth

import javax.inject.Inject

class SignInUseCase
    @Inject
    constructor(
        private val repository: AuthRepository,
    ) {
        suspend operator fun invoke(
            email: String,
            password: String,
        ): AuthResult<AuthUser> = repository.signIn(email.trim(), password)
    }
