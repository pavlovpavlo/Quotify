package com.kovhan.core.models.widget

/** Saved appearance for every widget style, kept independently of which is selected. */
data class WidgetAppearance(
    val minimal: WidgetStyleSettings.Minimal = WidgetStyleSettings.Minimal(),
    val classic: WidgetStyleSettings.Classic = WidgetStyleSettings.Classic(),
    val cover: WidgetStyleSettings.Cover = WidgetStyleSettings.Cover(),
) {
    operator fun get(style: WidgetStyle): WidgetStyleSettings = when (style) {
        WidgetStyle.MINIMAL -> minimal
        WidgetStyle.CLASSIC -> classic
        WidgetStyle.COVER -> cover
    }

    fun with(settings: WidgetStyleSettings): WidgetAppearance = when (settings) {
        is WidgetStyleSettings.Minimal -> copy(minimal = settings)
        is WidgetStyleSettings.Classic -> copy(classic = settings)
        is WidgetStyleSettings.Cover -> copy(cover = settings)
    }
}
