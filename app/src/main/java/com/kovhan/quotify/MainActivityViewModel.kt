package com.kovhan.quotify

import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.quotify.mvi.MainActivityEffect
import com.kovhan.quotify.mvi.MainActivityState
import com.kovhan.quotify.mvi.MainIntent
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(

): com.kovhan.core.ui.view_model.BaseViewModel<MainActivityState, MainActivityEffect>(MainActivityState()), MainIntent {
}