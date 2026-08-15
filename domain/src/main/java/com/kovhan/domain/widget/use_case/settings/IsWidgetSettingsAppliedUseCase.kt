package com.kovhan.domain.widget.use_case.settings

import com.kovhan.core.models.widget.WidgetSettings
import com.kovhan.domain.widget.WidgetSettingsRepository
import javax.inject.Inject

class IsWidgetSettingsAppliedUseCase @Inject constructor(
    private val repository: WidgetSettingsRepository,
) {
    suspend operator fun invoke(settings: WidgetSettings): Boolean = repository.isApplied(settings)
}
