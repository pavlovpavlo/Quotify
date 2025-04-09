package com.kovhan.feature.main.presentation.favorites.mvi

import com.kovhan.core.ui.UiEffect

sealed class FavoritesScreenEffect : UiEffect {
    data object None : FavoritesScreenEffect()
} 