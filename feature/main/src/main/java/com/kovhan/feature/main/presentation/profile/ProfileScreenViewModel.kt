package com.kovhan.feature.main.presentation.profile

import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.feature.main.presentation.profile.mvi.ProfileScreenEffect
import com.kovhan.feature.main.presentation.profile.mvi.ProfileScreenIntent
import com.kovhan.feature.main.presentation.profile.mvi.ProfileScreenState
import javax.inject.Inject

class ProfileScreenViewModel @Inject constructor() : 
    BaseViewModel<ProfileScreenState, ProfileScreenEffect>(ProfileScreenState()), 
    ProfileScreenIntent {
} 