package com.kovhan.core.models.widget

/**
 * Quote colour on the widget. [Auto] follows whichever of light/dark reads best
 * on the current background; the explicit variants pin it regardless.
 */
sealed interface WidgetTextColor {

    data object Auto : WidgetTextColor

    data object Light : WidgetTextColor

    data object Dark : WidgetTextColor

    /** Free colour picked by the user, stored as a packed ARGB int. */
    data class Custom(val argb: Int) : WidgetTextColor

    companion object {
        val DEFAULT: WidgetTextColor = Auto
    }
}
