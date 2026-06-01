package com.kovhan.feature.auth.presentation.forgot_password.mvi

import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.ui.UiState

data class ForgotPasswordScreenState(
    val email: TextFieldValue = TextFieldValue(""),
    val isLoading: Boolean = false,
) : UiState {
    val canSubmit: Boolean
        get() = !isLoading && email.text.isNotBlank()
}
