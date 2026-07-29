package com.kovhan.domain.widget.use_case.settings

import com.kovhan.core.models.widget.WidgetSettings
import com.kovhan.domain.widget.WidgetSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWidgetSettingsUseCase @Inject constructor(
    private val repository: WidgetSettingsRepository,
) {
    operator fun invoke(): Flow<WidgetSettings> = repository.observe()
}
