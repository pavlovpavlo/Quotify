package com.kovhan.domain.settings.use_case

import com.kovhan.domain.settings.AppTheme
import com.kovhan.domain.settings.SettingsRepository
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(theme: AppTheme) = repository.setTheme(theme)
}
