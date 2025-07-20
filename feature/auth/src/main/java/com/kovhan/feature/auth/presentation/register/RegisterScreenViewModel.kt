package com.kovhan.feature.auth.presentation.register

import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.feature.auth.presentation.register.mvi.RegisterScreenEffect
import com.kovhan.feature.auth.presentation.register.mvi.RegisterScreenIntent
import com.kovhan.feature.auth.presentation.register.mvi.RegisterScreenState
import javax.inject.Inject

class RegisterScreenViewModel @Inject constructor() : 
    BaseViewModel<RegisterScreenState, RegisterScreenEffect>(RegisterScreenState()), 
    RegisterScreenIntent {
} 