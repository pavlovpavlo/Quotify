package com.kovhan.domain.auth

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(): Flow<AuthUser?> = userRepository.observeUser()
}
