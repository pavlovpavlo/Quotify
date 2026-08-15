package com.kovhan.core.models.widget

/** Quote type scale on the widget. [scale] multiplies the style's base size. */
enum class WidgetFontSize(val scale: Float) {
    SMALL(0.81f),
    MEDIUM(1f),
    LARGE(1.19f),
    EXTRA_LARGE(1.43f),
    ;

    companion object {
        val DEFAULT = MEDIUM
    }
}
