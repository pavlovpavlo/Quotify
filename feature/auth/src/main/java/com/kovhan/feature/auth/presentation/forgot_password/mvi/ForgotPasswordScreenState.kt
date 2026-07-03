package com.kovhan.feature.auth.presentation.forgot_password.mvi

import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.ui.UiState
import com.kovhan.domain.auth.model.AuthError
import com.kovhan.domain.auth.model.ValidationError

data class ForgotPasswordScreenState(
    val email: TextFieldValue = TextFieldValue(""),
    val isLoading: Boolean = false,
    val errorValidationMessage: ValidationError? = null,
    val errorMessage: AuthError? = null
) : UiState {
    val canSubmit: Boolean
        get() = !isLoading && email.text.isNotBlank() &&
                errorValidationMessage == null &&
                errorMessage == null
}
