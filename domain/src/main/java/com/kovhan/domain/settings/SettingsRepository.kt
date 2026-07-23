package com.kovhan.domain.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeTheme(): Flow<AppTheme>

    suspend fun setTheme(theme: AppTheme)

    fun observeLanguage(): Flow<AppLanguage>

    suspend fun setLanguage(language: AppLanguage)

    fun observeDailyQuoteEnabled(): Flow<Boolean>

    suspend fun setDailyQuoteEnabled(enabled: Boolean)
}
