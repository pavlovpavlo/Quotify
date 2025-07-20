package com.kovhan.feature.auth.presentation.forgot_password.mvi

import com.kovhan.core.ui.UiState

data class ForgotPasswordScreenState(
    val isLoading: Boolean = false
) : UiState 