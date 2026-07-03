package com.kovhan.domain.auth.use_case

import com.kovhan.domain.auth.AuthRepository
import com.kovhan.domain.auth.model.AuthResult
import com.kovhan.core.models.AuthUser
import javax.inject.Inject

class SignUpUseCase
    @Inject
    constructor(
        private val repository: AuthRepository,
    ) {
        suspend operator fun invoke(
            email: String,
            password: String,
            displayName: String?,
        ): AuthResult<AuthUser> =
            repository.signUp(
                email = email.trim(),
                password = password,
                displayName = displayName?.trim()?.takeIf { it.isNotEmpty() },
            )
    }
