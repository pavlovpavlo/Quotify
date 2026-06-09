package com.kovhan.quotify.mvi

import com.kovhan.core.ui.UiState

data class MainActivityState(
    val isDarkMode: Boolean = false
) : UiState
