package com.kovhan.domain.widget.use_case.settings

import com.kovhan.domain.widget.WidgetSettingsRepository
import javax.inject.Inject

class SetWidgetFrequencyUseCase @Inject constructor(
    private val repository: WidgetSettingsRepository,
) {
    suspend operator fun invoke(hours: Int) = repository.setFrequencyHours(hours)
}
