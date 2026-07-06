package com.kovhan.domain.auth.use_case

import com.kovhan.domain.auth.UserRepository
import javax.inject.Inject

class UpdateUsernameUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(username: String) =
        userRepository.setUsername(username.trim().replace("@",""))
}
