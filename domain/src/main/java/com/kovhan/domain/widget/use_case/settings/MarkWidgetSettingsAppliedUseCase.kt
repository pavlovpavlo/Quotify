package com.kovhan.domain.widget.use_case.settings

import com.kovhan.core.models.widget.WidgetSettings
import com.kovhan.domain.widget.WidgetSettingsRepository
import javax.inject.Inject

class MarkWidgetSettingsAppliedUseCase @Inject constructor(
    private val repository: WidgetSettingsRepository,
) {
    suspend operator fun invoke(settings: WidgetSettings) = repository.rememberApplied(settings)
}
