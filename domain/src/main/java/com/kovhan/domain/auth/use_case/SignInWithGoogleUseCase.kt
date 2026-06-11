package com.kovhan.domain.auth.use_case

import com.kovhan.domain.auth.AuthRepository
import com.kovhan.domain.auth.AuthResult
import com.kovhan.domain.auth.AuthUser
import javax.inject.Inject

class SignInWithGoogleUseCase
    @Inject
    constructor(
        private val repository: AuthRepository,
    ) {
        suspend operator fun invoke(idToken: String): AuthResult<AuthUser> = repository.signInWithGoogle(idToken)
    }
