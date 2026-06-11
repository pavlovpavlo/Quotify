package com.kovhan.feature.main.presentation.edit_profile.mvi

import com.kovhan.core.ui.UiEffect

sealed class EditProfileEffect : UiEffect {
    data object NavigateToAuth : EditProfileEffect()
}
