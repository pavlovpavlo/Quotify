package com.kovhan.core.models.widget

/**
 * What the appearance editor persists for one [WidgetStyle]. The variants differ
 * only in how the background is chosen; the frame and type controls are shared,
 * so every style can be framed.
 */
sealed interface WidgetStyleSettings {

    val style: WidgetStyle
    val borderEnabled: Boolean
    val borderColor: WidgetBorderColor
    val fontSize: WidgetFontSize
    val textColor: WidgetTextColor
    val textAlign: WidgetTextAlign

    data class Minimal(
        override val borderEnabled: Boolean = false,
        override val borderColor: WidgetBorderColor = WidgetBorderColor.DEFAULT,
        override val fontSize: WidgetFontSize = WidgetFontSize.DEFAULT,
        override val textColor: WidgetTextColor = WidgetTextColor.DEFAULT,
        override val textAlign: WidgetTextAlign = WidgetTextAlign.DEFAULT,
    ) : WidgetStyleSettings {
        override val style: WidgetStyle get() = WidgetStyle.MINIMAL
    }

    data class Classic(
        val toneId: String = WidgetTones.DEFAULT,
        override val borderEnabled: Boolean = false,
        override val borderColor: WidgetBorderColor = WidgetBorderColor.DEFAULT,
        override val fontSize: WidgetFontSize = WidgetFontSize.DEFAULT,
        override val textColor: WidgetTextColor = WidgetTextColor.DEFAULT,
        override val textAlign: WidgetTextAlign = WidgetTextAlign.DEFAULT,
    ) : WidgetStyleSettings {
        override val style: WidgetStyle get() = WidgetStyle.CLASSIC
    }

    data class Cover(
        val coverId: String = WidgetCovers.DEFAULT,
        val blurEnabled: Boolean = false,
        override val borderEnabled: Boolean = false,
        override val borderColor: WidgetBorderColor = WidgetBorderColor.DEFAULT,
        override val fontSize: WidgetFontSize = WidgetFontSize.DEFAULT,
        override val textColor: WidgetTextColor = WidgetTextColor.DEFAULT,
        override val textAlign: WidgetTextAlign = WidgetTextAlign.DEFAULT,
    ) : WidgetStyleSettings {
        override val style: WidgetStyle get() = WidgetStyle.COVER
    }
}
