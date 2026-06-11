package com.kovhan.feature.main.presentation.profile.mvi

import com.kovhan.core.ui.UiState
import com.kovhan.domain.auth.AuthUser
import com.kovhan.domain.settings.AppLanguage
import com.kovhan.domain.settings.AppTheme

data class ProfileScreenState(
    val user: AuthUser? = null,
    val isLoading: Boolean = false,
    val isSigningOut: Boolean = false,
    val stats: ProfileStats = ProfileStats(),
    val showQuoteOfDay: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val reminderHour: Int = 9,
    val reminderMinute: Int = 0,
    val isTimePickerVisible: Boolean = false,
    val theme: AppTheme = AppTheme.SYSTEM,
    val language: AppLanguage = AppLanguage.UKRAINIAN,
    val isThemeSheetVisible: Boolean = false,
    val isLanguageSheetVisible: Boolean = false,
) : UiState

data class ProfileStats(
    val quotes: Int = 248,
    val books: Int = 32,
    val folders: Int = 9,
    val authors: Int = 57,
)
