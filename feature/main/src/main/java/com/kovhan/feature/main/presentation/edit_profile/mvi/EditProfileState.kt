package com.kovhan.feature.main.presentation.edit_profile.mvi

import com.kovhan.core.ui.UiState
import com.kovhan.core.models.AuthUser

data class EditProfileState(
    val user: AuthUser? = null,
    val isProcessing: Boolean = false,
) : UiState
