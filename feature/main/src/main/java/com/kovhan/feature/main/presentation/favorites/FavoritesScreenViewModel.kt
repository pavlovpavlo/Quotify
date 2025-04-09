package com.kovhan.feature.main.presentation.favorites

import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.feature.main.presentation.favorites.mvi.FavoritesScreenEffect
import com.kovhan.feature.main.presentation.favorites.mvi.FavoritesScreenIntent
import com.kovhan.feature.main.presentation.favorites.mvi.FavoritesScreenState
import javax.inject.Inject

class FavoritesScreenViewModel @Inject constructor() : 
    BaseViewModel<FavoritesScreenState, FavoritesScreenEffect>(FavoritesScreenState()), 
    FavoritesScreenIntent {
} 