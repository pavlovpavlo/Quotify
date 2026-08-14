package com.kovhan.design.systems

import androidx.compose.ui.graphics.Color

/**
 * Folio Design System — raw color tokens (light + dark).
 * Warm, editorial reading app. "Paper + ink + warm accents."
 *
 * Do not consume these directly from screens — use [QuotifyColorPalette]
 * through `QuotifyMaterialTheme.colors.*` so light/dark switching works.
 */
object FolioLight {
    // Surfaces
    val bgPrimary   = Color(0xFFFAF7F2) // "paper" — the canvas
    val bgSecondary = Color(0xFFE8E2D5) // cards, sections, sidebars
    val bgElevated  = Color(0xFFFFFFFF) // menus, dialogs
    val bgOverlay   = Color(0x731A1714) // scrims (rgba 26,23,20,0.45)

    // Text
    val textPrimary   = Color(0xFF1A1714) // "ink"
    val textSecondary = Color(0xFF6B6256)
    val textTertiary  = Color(0xFF9A9183)
    val textOnAccent  = Color(0xFFFAF7F2)

    // Borders
    val border        = Color(0xFFE8E2D5)
    val borderStrong  = Color(0xFFD4CCBC)
    val borderSubtle  = Color(0xFFEFEAE0)

    // Accent — Terracotta (primary)
    val accentPrimary       = Color(0xFFC8553D)
    val accentPrimaryHover  = Color(0xFFB14A34)
    val accentPrimarySoft   = Color(0xFFF5E5DF)

    // Accent — AI (blue)
    val accentAi       = Color(0xFF3D5A6C)
    val accentAiHover  = Color(0xFF344E5E)
    val accentAiSoft   = Color(0xFFE3E9ED)

    // Accent — Saved (olive)
    val accentSaved       = Color(0xFF7A8B5C)
    val accentSavedHover  = Color(0xFF6A7B4F)
    val accentSavedSoft   = Color(0xFFEAEEDF)

    // Accent — Premium (gold)
    val accentPremium       = Color(0xFFD4A056)
    val accentPremiumHover  = Color(0xFFBE8E48)
    val accentPremiumSoft   = Color(0xFFF7EDDA)

    // Accent — Plum (fixed hex, shared light/dark)
    val accentPlum = Color(0xFF9A5C74)

    // Accent — Teal (fixed hex, shared light/dark)
    val accentTeal = Color(0xFF4E867B)

    // Status — error (not part of Folio spec, kept compatible with Quotify)
    val error     = Color(0xFFC7402F)
    val errorSoft = Color(0xFFF5E5DF)
}

object FolioDark {
    // Surfaces — warm dark, not pure inversion
    val bgPrimary   = Color(0xFF1A1714)
    val bgSecondary = Color(0xFF2B2622)
    val bgElevated  = Color(0xFF332E29)
    val bgOverlay   = Color(0x8C000000) // rgba 0,0,0,0.55

    // Text
    val textPrimary   = Color(0xFFF0EBE0)
    val textSecondary = Color(0xFF8A8278)
    val textTertiary  = Color(0xFF5F584F)
    val textOnAccent  = Color(0xFF1A1714)

    // Borders
    val border        = Color(0xFF3A342E)
    val borderStrong  = Color(0xFF4A433C)
    val borderSubtle  = Color(0xFF2F2A26)

    // Accent — Terracotta
    val accentPrimary       = Color(0xFFE07659)
    val accentPrimaryHover  = Color(0xFFE88670)
    val accentPrimarySoft   = Color(0xFF3D2A24)

    // Accent — AI
    val accentAi       = Color(0xFF5A8FA8)
    val accentAiHover  = Color(0xFF6FA1B8)
    val accentAiSoft   = Color(0xFF243239)

    // Accent — Saved
    val accentSaved       = Color(0xFF94A878)
    val accentSavedHover  = Color(0xFFA4B888)
    val accentSavedSoft   = Color(0xFF2E3527)

    // Accent — Premium
    val accentPremium       = Color(0xFFE5B870)
    val accentPremiumHover  = Color(0xFFECC585)
    val accentPremiumSoft   = Color(0xFF3A3024)

    // Accent — Plum (fixed hex, shared light/dark)
    val accentPlum = Color(0xFF9A5C74)

    // Accent — Teal (fixed hex, shared light/dark)
    val accentTeal = Color(0xFF4E867B)

    // Status — error
    val error     = Color(0xFFE07659)
    val errorSoft = Color(0xFF3D2A24)
}

/**
 * Premium surfaces — the olive gradient card and CTA. Fixed hex, shared
 * light/dark: the surface stays olive in both themes and always carries white
 * text, so theme-aware roles would break its contrast.
 */
object FolioPremium {
    // Gradient stops
    val gradientStart  = Color(0xFF84956A)
    val gradientMiddle = Color(0xFF7A8B5C)
    val gradientEnd    = Color(0xFF66794B)

    // Content on the gradient
    val onGradient       = Color(0xFFFFFFFF)
    val onGradientMuted  = Color(0xD9FFFFFF)
    val onGradientSubtle = Color(0xB3FFFFFF)

    // Inner white card nested on the gradient
    val card           = Color(0xFFFFFFFF)
    val cardDivider    = Color(0xFFEFEAE0)
    val cardText       = Color(0xFF1A1714)
    val iconBackground = Color(0xFFEAEEDF)
    val iconTint       = Color(0xFF66794B)
}

/**
 * Special-offer screen. The warm hero stays identical in both themes — the
 * illustration on it carries its own light, so a dark variant would break it.
 * Only the close button, which sits on top of the artwork, flips.
 */
object FolioOffer {
    val heroGradientStart = Color(0xFFF3E4CF)
    val heroGradientEnd   = Color(0xFFEBCBA8)

    /** Deep terracotta the CTA gradient runs into, from `accentPrimary`. */
    val ctaGradientEnd = Color(0xFFA8442F)

    val closeScrimLight = Color(0xB3FFFFFF)
    val closeScrimDark  = Color(0x59000000)
    val closeIconLight  = Color(0xFF3A2A20)
    val closeIconDark   = Color(0xFFFAF7F2)
}

/**
 * Legacy alias retained only for [com.kovhan.core.ui.extensions.ComposeExtension]
 * skeleton gradient. New code should use [QuotifyMaterialTheme.colors] instead.
 */
object Colors {
    val primary   = FolioLight.accentPrimary
    val tabBg     = FolioLight.accentPrimarySoft
    val black     = Color(0xFF000000)
    val white     = Color(0xFFFFFFFF)
}
