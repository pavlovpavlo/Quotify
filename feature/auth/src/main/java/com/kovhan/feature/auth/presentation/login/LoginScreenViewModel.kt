package com.kovhan.feature.auth.presentation.login

import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.feature.auth.presentation.login.mvi.LoginScreenEffect
import com.kovhan.feature.auth.presentation.login.mvi.LoginScreenIntent
import com.kovhan.feature.auth.presentation.login.mvi.LoginScreenState
import javax.inject.Inject

class LoginScreenViewModel @Inject constructor() : 
    BaseViewModel<LoginScreenState, LoginScreenEffect>(LoginScreenState()), 
    LoginScreenIntent {
} 