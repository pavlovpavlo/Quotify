package com.kovhan.domain.auth.use_case

import com.kovhan.domain.auth.AuthRepository
import com.kovhan.domain.auth.AuthResult
import com.kovhan.domain.auth.AuthUser
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
