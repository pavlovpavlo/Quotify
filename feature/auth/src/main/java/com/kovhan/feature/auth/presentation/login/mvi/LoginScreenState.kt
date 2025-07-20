package com.kovhan.feature.auth.presentation.login.mvi

import com.kovhan.core.ui.UiState

data class LoginScreenState(
    val isLoading: Boolean = false
) : UiState 