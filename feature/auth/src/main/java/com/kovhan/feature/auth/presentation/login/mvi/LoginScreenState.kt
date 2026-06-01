package com.kovhan.feature.auth.presentation.login.mvi

import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.ui.UiState

data class LoginScreenState(
    val email: TextFieldValue = TextFieldValue(""),
    val password: TextFieldValue = TextFieldValue(""),
    val isLoading: Boolean = false,
    val isGoogleLoading: Boolean = false,
) : UiState {
    val canSubmit: Boolean
        get() = !isLoading && !isGoogleLoading && email.text.isNotBlank() && password.text.isNotBlank()
}
