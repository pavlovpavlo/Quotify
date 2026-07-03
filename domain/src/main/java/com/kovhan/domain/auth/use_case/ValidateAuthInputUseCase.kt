package com.kovhan.domain.auth.use_case

import com.kovhan.domain.auth.model.ValidationError
import javax.inject.Inject

class ValidateAuthInputUseCase @Inject constructor(
    private val validator: AuthValidator,
) {
    operator fun invoke(
        name: String? = null,
        email: String? = null,
        password: String? = null,
    ): ValidationError? = when {
        name != null && !validator.isNameValid(name) -> ValidationError.EmptyName
        email != null && !validator.isEmailValid(email) -> ValidationError.InvalidEmail
        password != null && !validator.isPasswordValid(password) -> ValidationError.ShortPassword
        else -> null
    }
}
