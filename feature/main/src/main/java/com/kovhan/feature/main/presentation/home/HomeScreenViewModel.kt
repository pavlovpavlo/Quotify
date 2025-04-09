package com.kovhan.feature.main.presentation.home

import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.feature.main.presentation.home.mvi.HomeScreenEffect
import com.kovhan.feature.main.presentation.home.mvi.HomeScreenIntent
import com.kovhan.feature.main.presentation.home.mvi.HomeScreenState
import javax.inject.Inject

class HomeScreenViewModel @Inject constructor() : 
    BaseViewModel<HomeScreenState, HomeScreenEffect>(HomeScreenState()), 
    HomeScreenIntent {
} 