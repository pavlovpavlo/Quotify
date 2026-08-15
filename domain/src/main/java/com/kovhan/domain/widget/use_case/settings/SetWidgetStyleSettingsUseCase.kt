package com.kovhan.domain.widget.use_case.settings

import com.kovhan.core.models.widget.WidgetStyleSettings
import com.kovhan.domain.widget.WidgetSettingsRepository
import javax.inject.Inject

class SetWidgetStyleSettingsUseCase @Inject constructor(
    private val repository: WidgetSettingsRepository,
) {
    suspend operator fun invoke(settings: WidgetStyleSettings) =
        repository.setStyleSettings(settings)
}
