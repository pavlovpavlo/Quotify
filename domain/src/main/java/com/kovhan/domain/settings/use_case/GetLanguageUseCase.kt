package com.kovhan.domain.settings.use_case

import com.kovhan.domain.settings.AppLanguage
import com.kovhan.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    operator fun invoke(): Flow<AppLanguage> = repository.observeLanguage()
}
