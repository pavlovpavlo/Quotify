package com.kovhan.feature.main.presentation.favorites.mvi

import com.kovhan.core.ui.UiState

data class FavoritesScreenState(
    val isLoading: Boolean = false
) : UiState 