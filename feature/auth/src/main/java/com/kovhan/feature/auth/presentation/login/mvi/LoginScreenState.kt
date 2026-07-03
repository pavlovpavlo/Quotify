package com.kovhan.feature.auth.presentation.login.mvi

import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.ui.UiState
import com.kovhan.domain.auth.model.AuthError
import com.kovhan.domain.auth.model.ValidationError

data class LoginScreenState(
    val email: TextFieldValue = TextFieldValue(""),
    val password: TextFieldValue = TextFieldValue(""),
    val isLoading: Boolean = false,
    val isGoogleLoading: Boolean = false,
    val errorMessage: AuthError? = null,
    val errorValidationMessage: ValidationError? = null,
) : UiState {
    val canSubmit: Boolean
        get() = !isLoading && !isGoogleLoading && email.text.isNotBlank() && password.text.isNotBlank()&&
                errorValidationMessage == null &&
                errorMessage == null
}
