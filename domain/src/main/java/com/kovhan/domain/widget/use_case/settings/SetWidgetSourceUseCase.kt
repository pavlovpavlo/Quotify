package com.kovhan.domain.widget.use_case.settings

import com.kovhan.core.models.widget.WidgetSource
import com.kovhan.domain.widget.WidgetSettingsRepository
import javax.inject.Inject

class SetWidgetSourceUseCase @Inject constructor(
    private val repository: WidgetSettingsRepository,
) {
    suspend operator fun invoke(source: WidgetSource) = repository.setSource(source)
}
