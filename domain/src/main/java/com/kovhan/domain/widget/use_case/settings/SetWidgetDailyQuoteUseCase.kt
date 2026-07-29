package com.kovhan.domain.widget.use_case.settings

import com.kovhan.domain.widget.WidgetSettingsRepository
import javax.inject.Inject

class SetWidgetDailyQuoteUseCase @Inject constructor(
    private val repository: WidgetSettingsRepository,
) {
    suspend operator fun invoke(enabled: Boolean) = repository.setIncludeDailyQuote(enabled)
}
