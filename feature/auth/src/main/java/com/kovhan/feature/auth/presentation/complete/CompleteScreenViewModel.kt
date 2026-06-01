package com.kovhan.feature.auth.presentation.complete

import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.feature.auth.presentation.complete.mvi.CompleteScreenEffect
import com.kovhan.feature.auth.presentation.complete.mvi.CompleteScreenIntent
import com.kovhan.feature.auth.presentation.complete.mvi.CompleteScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompleteScreenViewModel @Inject constructor() :
    BaseViewModel<CompleteScreenState, CompleteScreenEffect>(CompleteScreenState),
    CompleteScreenIntent {

    override fun onSignUpClicked() {
        publishEffect(CompleteScreenEffect.NavigateToSignUp)
    }

    override fun onSignInClicked() {
        publishEffect(CompleteScreenEffect.NavigateToSignIn)
    }

    override fun onLaterClicked() {
        publishEffect(CompleteScreenEffect.NavigateToMain)
    }
}
