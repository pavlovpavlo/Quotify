package com.kovhan.domain.auth.use_case

import com.kovhan.domain.auth.UserRepository
import javax.inject.Inject

class RemoveProfilePhotoUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke() = userRepository.clearCustomPhoto()
}
