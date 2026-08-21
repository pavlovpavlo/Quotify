package com.kovhan.feature.main.presentation.profile.mvi

import com.kovhan.core.ui.UiState
import com.kovhan.core.models.AuthUser
import com.kovhan.core.models.profile.ProfileStatistic
import com.kovhan.domain.settings.AppLanguage
import com.kovhan.domain.settings.AppTheme

data class ProfileScreenState(
    val user: AuthUser? = null,
    val isLoading: Boolean = false,
    val isSigningOut: Boolean = false,
    val stats: ProfileStatistic = ProfileStatistic(),
    val isPremium: Boolean = false,
    val showQuoteOfDay: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val reminderHour: Int = 9,
    val reminderMinute: Int = 0,
    val theme: AppTheme = AppTheme.SYSTEM,
    val language: AppLanguage = AppLanguage.default,
    val postponedSurveyId: String? = null,
) : UiState

