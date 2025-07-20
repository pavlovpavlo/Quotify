package com.kovhan.feature.auth.presentation.forgot_password

import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.feature.auth.presentation.forgot_password.mvi.ForgotPasswordScreenEffect
import com.kovhan.feature.auth.presentation.forgot_password.mvi.ForgotPasswordScreenIntent
import com.kovhan.feature.auth.presentation.forgot_password.mvi.ForgotPasswordScreenState
import javax.inject.Inject

class ForgotPasswordScreenViewModel @Inject constructor() : 
    BaseViewModel<ForgotPasswordScreenState, ForgotPasswordScreenEffect>(ForgotPasswordScreenState()), 
    ForgotPasswordScreenIntent {
} 