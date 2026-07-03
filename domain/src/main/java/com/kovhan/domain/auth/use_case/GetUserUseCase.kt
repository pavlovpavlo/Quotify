package com.kovhan.domain.auth.use_case

import com.kovhan.core.models.AuthUser
import com.kovhan.domain.auth.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(): Flow<AuthUser?> = userRepository.observeUser()
}
