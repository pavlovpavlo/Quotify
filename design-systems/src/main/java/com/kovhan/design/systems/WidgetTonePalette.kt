package com.kovhan.design.systems

import androidx.compose.ui.graphics.Color

/**
 * Background fills for the home-screen widget. Fixed rather than theme-aware:
 * the widget lives on the launcher's wallpaper, so its look is the user's own
 * choice and must not flip with the app's light/dark mode.
 *
 * Keyed by the tone tokens in `core:models` — resolve through
 * `core:ui`'s widget background mapper, never by reading these directly.
 */
object WidgetTonePalette {

    val papier = Color(0xFFFFFFFF)
    val terra  = Color(0xFFC8553D)
    val ai     = Color(0xFF4A6577)
    val olive  = Color(0xFF5A6B4A)
    val gold   = Color(0xFFD4A056)
    val ink    = Color(0xFF1A1714)
    val plum   = Color(0xFF9A5C74)
    val teal   = Color(0xFF4E867B)
    val sand   = Color(0xFFEDE3D0)
    val blush  = Color(0xFFE8B4A0)
    val rust   = Color(0xFF869A5A)
    val forest = Color(0xFF3A5240)
    val slate  = Color(0xFF4A4640)
    val clay   = Color(0xFFC08A6A)
    val wine   = Color(0xFF7A2E38)

    /** Quote colour on a light fill. */
    val textOnLight = Color(0xFF1A1714)

    /** Quote colour on a dark fill. */
    val textOnDark = Color(0xFFFAF7F2)

    /** Border of the minimal style — fixed, not user-editable. */
    val minimalBorder = Color(0xFF1A1714)
}
