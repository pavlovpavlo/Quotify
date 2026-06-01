package com.kovhan.domain.auth

import android.util.Patterns
import javax.inject.Inject

class AuthValidator
    @Inject
    constructor() {
        fun isNameValid(name: String): Boolean = name.trim().length >= MIN_NAME_LENGTH

        fun isEmailValid(email: String): Boolean {
            val value = email.trim()
            return value.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(value).matches()
        }

        fun isPasswordValid(password: String): Boolean = password.length >= MIN_PASSWORD_LENGTH

        private companion object {
            const val MIN_NAME_LENGTH = 1
            const val MIN_PASSWORD_LENGTH = 6
        }
    }
