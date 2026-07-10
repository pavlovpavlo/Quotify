package com.kovhan.feature.auth.presentation.complete.mvi

import com.kovhan.core.ui.UiState

data class CompleteScreenState(
    val isGuestLoading: Boolean = false,
) : UiState
