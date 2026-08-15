package com.kovhan.core.ui.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.kovhan.core.models.widget.WidgetBorderColor
import com.kovhan.core.models.widget.WidgetStyleSettings
import com.kovhan.core.models.widget.WidgetTextColor
import com.kovhan.core.models.widget.WidgetTones
import com.kovhan.design.systems.WidgetTonePalette

data class WidgetBackgroundColors(
    val fill: Color,
    val border: Color,
    val readableText: Color,
)

/**
 * Resolves widget tone tokens into the colours the preview and the rendered
 * widget both draw with. Borders are derived from the fill rather than stored as
 * their own token, so every tone gets a frame that belongs to it.
 */
object WidgetBackgroundMapper {

    private const val BORDER_DARKEN = 0.78f
    private const val LIGHT_FILL_LUMINANCE = 0.45f

    fun fill(toneId: String): Color = when (toneId.lowercase()) {
        "papier" -> WidgetTonePalette.papier
        "terra" -> WidgetTonePalette.terra
        "ai" -> WidgetTonePalette.ai
        "olive" -> WidgetTonePalette.olive
        "gold" -> WidgetTonePalette.gold
        "ink" -> WidgetTonePalette.ink
        "plum" -> WidgetTonePalette.plum
        "teal" -> WidgetTonePalette.teal
        "sand" -> WidgetTonePalette.sand
        "blush" -> WidgetTonePalette.blush
        "rust" -> WidgetTonePalette.rust
        "forest" -> WidgetTonePalette.forest
        "slate" -> WidgetTonePalette.slate
        "clay" -> WidgetTonePalette.clay
        "wine" -> WidgetTonePalette.wine
        else -> fill(WidgetTones.DEFAULT)
    }

    fun colors(toneId: String): WidgetBackgroundColors {
        val fill = fill(toneId)
        return WidgetBackgroundColors(
            fill = fill,
            border = fill.darken(BORDER_DARKEN),
            readableText = readableTextOn(fill),
        )
    }

    /**
     * Colour painted behind the quote. The minimal and cover styles paint none —
     * the wallpaper and the photo show through instead.
     */
    fun fillColor(settings: WidgetStyleSettings): Color = when (settings) {
        is WidgetStyleSettings.Minimal -> Color.Transparent
        is WidgetStyleSettings.Classic -> fill(settings.toneId)
        is WidgetStyleSettings.Cover -> Color.Transparent
    }

    /**
     * The quote colour to draw.
     *
     * The minimal style has no fill of its own, so the wallpaper behind it is
     * whatever the launcher shows and only the theme is a reliable signal: it
     * always follows [darkTheme], overriding even an explicit light pick that
     * would be invisible on a light theme.
     */
    fun textColor(settings: WidgetStyleSettings, darkTheme: Boolean): Color {
        val chosen = settings.textColor

        if (settings is WidgetStyleSettings.Minimal) {
            if (chosen is WidgetTextColor.Custom) return Color(chosen.argb)
            return if (darkTheme) WidgetTonePalette.textOnDark else WidgetTonePalette.textOnLight
        }

        if (chosen !is WidgetTextColor.Auto) return textColor(chosen, Color.Transparent)

        return when (settings) {
            is WidgetStyleSettings.Classic -> readableTextOn(fill(settings.toneId))

            is WidgetStyleSettings.Cover ->
                readableTextOn(WidgetCoverMapper.averageColor(settings.coverId, settings.blurEnabled))

            is WidgetStyleSettings.Minimal -> WidgetTonePalette.textOnLight
        }
    }

    fun textColor(textColor: WidgetTextColor, background: Color): Color = when (textColor) {
        WidgetTextColor.Auto -> readableTextOn(background)
        WidgetTextColor.Light -> WidgetTonePalette.textOnDark
        WidgetTextColor.Dark -> WidgetTonePalette.textOnLight
        is WidgetTextColor.Custom -> Color(textColor.argb)
    }

    fun readableTextOn(background: Color): Color =
        if (background.luminance() > LIGHT_FILL_LUMINANCE) {
            WidgetTonePalette.textOnLight
        } else {
            WidgetTonePalette.textOnDark
        }

    /** Border of [settings], or null when it draws none. */
    fun borderColor(settings: WidgetStyleSettings, darkTheme: Boolean): Color? {
        if (!settings.borderEnabled) return null

        (settings.borderColor as? WidgetBorderColor.Tone)?.let { return fill(it.toneId) }

        return when (settings) {
            // No fill to derive from — the frame tracks the theme so it stays visible.
            is WidgetStyleSettings.Minimal ->
                if (darkTheme) WidgetTonePalette.textOnDark else WidgetTonePalette.minimalBorder

            is WidgetStyleSettings.Classic -> colors(settings.toneId).border

            is WidgetStyleSettings.Cover ->
                WidgetCoverMapper.averageColor(settings.coverId, settings.blurEnabled)
                    .darken(BORDER_DARKEN)
        }
    }

    private fun Color.darken(factor: Float) = Color(
        red = red * factor,
        green = green * factor,
        blue = blue * factor,
        alpha = alpha,
    )
}
