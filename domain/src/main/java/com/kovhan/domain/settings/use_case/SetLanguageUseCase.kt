package com.kovhan.domain.settings.use_case

import com.kovhan.domain.settings.AppLanguage
import com.kovhan.domain.settings.SettingsRepository
import javax.inject.Inject

class SetLanguageUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(language: AppLanguage) = repository.setLanguage(language)
}
