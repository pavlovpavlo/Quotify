package com.kovhan.domain.settings.use_case

import com.kovhan.domain.settings.SettingsRepository
import javax.inject.Inject

class SetDailyQuoteEnabledUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(enabled: Boolean) = repository.setDailyQuoteEnabled(enabled)
}
