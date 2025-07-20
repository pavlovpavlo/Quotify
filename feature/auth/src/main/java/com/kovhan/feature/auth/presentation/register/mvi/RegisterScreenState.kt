package com.kovhan.feature.auth.presentation.register.mvi

import com.kovhan.core.ui.UiState

data class RegisterScreenState(
    val isLoading: Boolean = false
) : UiState 