package com.kovhan.feature.auth.presentation.register.mvi

import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.ui.UiState
import com.kovhan.domain.auth.model.AuthError
import com.kovhan.domain.auth.model.ValidationError

data class RegisterScreenState(
    val fullName: TextFieldValue = TextFieldValue(""),
    val username: TextFieldValue = TextFieldValue(""),
    val email: TextFieldValue = TextFieldValue(""),
    val password: TextFieldValue = TextFieldValue(""),
    val termsAccepted: Boolean = false,
    val isLoading: Boolean = false,
    val isGoogleLoading: Boolean = false,
    val errorMessage: AuthError? = null,
    val errorValidationMessage: ValidationError? = null,

) : UiState {
    val canSubmit: Boolean
        get() = !isLoading &&
            !isGoogleLoading &&
            username.text.isNotBlank() &&
            email.text.isNotBlank() &&
            password.text.isNotBlank() &&
            termsAccepted &&
            errorValidationMessage == null &&
            errorMessage == null
}
