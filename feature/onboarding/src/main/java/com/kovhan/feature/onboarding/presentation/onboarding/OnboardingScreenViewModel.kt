package com.kovhan.feature.onboarding.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kovhan.feature.onboarding.presentation.onboarding.mvi.OnboardingEffect
import com.kovhan.feature.onboarding.presentation.onboarding.mvi.OnboardingIntent
import com.kovhan.feature.onboarding.presentation.onboarding.mvi.OnboardingState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class OnboardingScreenViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state

    private val _effect = MutableSharedFlow<OnboardingEffect>()
    val effect: SharedFlow<OnboardingEffect> = _effect

} 