package com.kovhan.domain.settings.use_case

import com.kovhan.domain.settings.AppTheme
import com.kovhan.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    operator fun invoke(): Flow<AppTheme> = repository.observeTheme()
}
