package com.kovhan.domain.auth.use_case

import com.kovhan.domain.auth.AuthRepository
import com.kovhan.domain.auth.model.AuthResult
import com.kovhan.core.models.AuthUser
import javax.inject.Inject

class SignInWithGoogleUseCase
    @Inject
    constructor(
        private val repository: AuthRepository,
    ) {
        suspend operator fun invoke(idToken: String): AuthResult<AuthUser> = repository.signInWithGoogle(idToken)
    }
