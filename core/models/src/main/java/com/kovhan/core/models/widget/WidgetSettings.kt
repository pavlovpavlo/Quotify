package com.kovhan.core.models.widget

/** Everything the widget-settings screen persists for the home-screen widget. */
data class WidgetSettings(
    val source: WidgetSource = WidgetSource.All,
    val includeDailyQuote: Boolean = true,
    val frequencyHours: Int = DEFAULT_FREQUENCY_HOURS,
    val style: WidgetStyle = WidgetStyle.CLASSIC,
    val appearance: WidgetAppearance = WidgetAppearance(),
) {
    /** Appearance of the style currently selected for the home screen. */
    val activeStyleSettings: WidgetStyleSettings get() = appearance[style]

    companion object {
        const val DEFAULT_FREQUENCY_HOURS = 2
        const val MIN_FREQUENCY_HOURS = 1
        const val MAX_FREQUENCY_HOURS = 24
    }
}
