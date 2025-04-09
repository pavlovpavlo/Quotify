package com.kovhan.feature.main.presentation.quotes.mvi

import com.kovhan.core.ui.UiState

data class QuotesScreenState(
    val isLoading: Boolean = false
) : UiState 