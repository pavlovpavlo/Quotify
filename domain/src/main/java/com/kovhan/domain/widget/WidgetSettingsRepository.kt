package com.kovhan.domain.widget

import com.kovhan.core.models.widget.WidgetSettings
import com.kovhan.core.models.widget.WidgetSource
import com.kovhan.core.models.widget.WidgetStyle
import com.kovhan.core.models.widget.WidgetStyleSettings
import kotlinx.coroutines.flow.Flow

interface WidgetSettingsRepository {
    fun observe(): Flow<WidgetSettings>

    suspend fun setSource(source: WidgetSource)

    suspend fun setIncludeDailyQuote(enabled: Boolean)

    suspend fun setFrequencyHours(hours: Int)

    suspend fun setStyle(style: WidgetStyle)

    /** Persists one style's appearance; the other two stay as they were. */
    suspend fun setStyleSettings(settings: WidgetStyleSettings)
}
