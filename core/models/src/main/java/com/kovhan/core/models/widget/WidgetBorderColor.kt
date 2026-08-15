package com.kovhan.core.models.widget

/**
 * Widget border colour. [Auto] derives a slightly darker shade of the current
 * background so the frame always belongs to its fill; [Tone] pins one of the
 * background tones instead.
 */
sealed interface WidgetBorderColor {

    data object Auto : WidgetBorderColor

    data class Tone(val toneId: String) : WidgetBorderColor

    companion object {
        val DEFAULT: WidgetBorderColor = Auto
    }
}
