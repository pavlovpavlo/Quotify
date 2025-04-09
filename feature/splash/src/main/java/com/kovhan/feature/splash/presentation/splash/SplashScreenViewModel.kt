package com.kovhan.feature.splash.presentation.splash

import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.feature.splash.presentation.splash.mvi.SplashScreenIntent
import com.kovhan.feature.splash.presentation.splash.mvi.SplashScreenState
import com.kovhan.feature.splash.presentation.splash.mvi.SplashScreenEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashScreenViewModel @Inject
constructor(

): BaseViewModel<SplashScreenState, SplashScreenEffect>(SplashScreenState()), SplashScreenIntent {

    init {
        viewModelScope.launch {
            delay(4000)
            publishEffect(SplashScreenEffect.NavigateToMain)
        }
    }
}