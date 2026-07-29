package com.kovhan.domain.widget.use_case.settings

import com.kovhan.core.models.widget.WidgetStyle
import com.kovhan.domain.widget.WidgetSettingsRepository
import javax.inject.Inject

class SetWidgetStyleUseCase @Inject constructor(
    private val repository: WidgetSettingsRepository,
) {
    suspend operator fun invoke(style: WidgetStyle) = repository.setStyle(style)
}
