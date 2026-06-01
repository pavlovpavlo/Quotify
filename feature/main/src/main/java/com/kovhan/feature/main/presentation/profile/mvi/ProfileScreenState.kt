package com.kovhan.feature.main.presentation.profile.mvi

import com.kovhan.core.ui.UiState
import com.kovhan.domain.auth.AuthUser

data class ProfileScreenState(
    val user: AuthUser? = null,
    val isLoading: Boolean = false,
    val isSigningOut: Boolean = false,
) : UiState
