package com.kovhan.domain.auth.use_case

import com.kovhan.domain.auth.AuthRepository
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Boolean = authRepository.currentUser() != null
}
