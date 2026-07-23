package com.kovhan.domain.auth.use_case.guest

import com.kovhan.core.models.AuthUser
import com.kovhan.domain.auth.model.AuthResult
import javax.inject.Inject

class ContinueAsGuestUseCase @Inject constructor(
    private val upgrader: GuestAccountUpgrader,
) {
    suspend operator fun invoke(): AuthResult<AuthUser> = upgrader.continueAsGuest()
}
