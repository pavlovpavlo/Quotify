package com.kovhan.domain.auth.use_case

import javax.inject.Inject

class AuthValidator
    @Inject
    constructor() {
        fun isNameValid(name: String): Boolean = name.trim().length >= MIN_NAME_LENGTH

        fun isEmailValid(email: String): Boolean {
            val value = email.trim()
            return value.isNotEmpty() && EMAIL_REGEX.matches(value)
        }

        fun isPasswordValid(password: String): Boolean = password.trim().length >= MIN_PASSWORD_LENGTH

        private companion object {
            const val MIN_NAME_LENGTH = 1
            const val MIN_PASSWORD_LENGTH = 8
            val EMAIL_REGEX = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
        }
    }
