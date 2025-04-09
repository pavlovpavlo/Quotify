package com.kovhan.feature.main.presentation.home.mvi

import com.kovhan.core.ui.UiState

data class HomeScreenState(
    val isLoading: Boolean = false
) : UiState 