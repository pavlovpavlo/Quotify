package com.kovhan.domain.auth

import javax.inject.Inject

class SignInWithGoogleUseCase
    @Inject
    constructor(
        private val repository: AuthRepository,
    ) {
        suspend operator fun invoke(idToken: String): AuthResult<AuthUser> = repository.signInWithGoogle(idToken)
    }
