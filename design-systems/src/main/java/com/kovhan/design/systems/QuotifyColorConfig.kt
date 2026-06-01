package com.kovhan.design.systems

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocaleQuotifyColors = compositionLocalOf { quotifyLightPalette }

/**
 * Folio semantic color palette. Switch is light/dark aware via
 * [QuotifyAppTheme]. Always read colors through these roles, never
 * from [FolioLight] / [FolioDark] directly in UI code.
 */
@Immutable
data class QuotifyColorPalette(
    // Surfaces
    val bgPrimary: Color,
    val bgSecondary: Color,
    val bgElevated: Color,
    val bgOverlay: Color,

    // Text
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textOnAccent: Color,

    // Borders
    val border: Color,
    val borderStrong: Color,
    val borderSubtle: Color,

    // Accent — Terracotta (primary action)
    val accentPrimary: Color,
    val accentPrimaryHover: Color,
    val accentPrimarySoft: Color,

    // Accent — AI (blue)
    val accentAi: Color,
    val accentAiHover: Color,
    val accentAiSoft: Color,

    // Accent — Saved (olive)
    val accentSaved: Color,
    val accentSavedHover: Color,
    val accentSavedSoft: Color,

    // Accent — Premium (gold)
    val accentPremium: Color,
    val accentPremiumHover: Color,
    val accentPremiumSoft: Color,

    // Status
    val error: Color,
    val errorSoft: Color,
)

val quotifyLightPalette = QuotifyColorPalette(
    bgPrimary   = FolioLight.bgPrimary,
    bgSecondary = FolioLight.bgSecondary,
    bgElevated  = FolioLight.bgElevated,
    bgOverlay   = FolioLight.bgOverlay,

    textPrimary   = FolioLight.textPrimary,
    textSecondary = FolioLight.textSecondary,
    textTertiary  = FolioLight.textTertiary,
    textOnAccent  = FolioLight.textOnAccent,

    border        = FolioLight.border,
    borderStrong  = FolioLight.borderStrong,
    borderSubtle  = FolioLight.borderSubtle,

    accentPrimary      = FolioLight.accentPrimary,
    accentPrimaryHover = FolioLight.accentPrimaryHover,
    accentPrimarySoft  = FolioLight.accentPrimarySoft,

    accentAi      = FolioLight.accentAi,
    accentAiHover = FolioLight.accentAiHover,
    accentAiSoft  = FolioLight.accentAiSoft,

    accentSaved      = FolioLight.accentSaved,
    accentSavedHover = FolioLight.accentSavedHover,
    accentSavedSoft  = FolioLight.accentSavedSoft,

    accentPremium      = FolioLight.accentPremium,
    accentPremiumHover = FolioLight.accentPremiumHover,
    accentPremiumSoft  = FolioLight.accentPremiumSoft,

    error     = FolioLight.error,
    errorSoft = FolioLight.errorSoft,
)

val quotifyDarkPalette = QuotifyColorPalette(
    bgPrimary   = FolioDark.bgPrimary,
    bgSecondary = FolioDark.bgSecondary,
    bgElevated  = FolioDark.bgElevated,
    bgOverlay   = FolioDark.bgOverlay,

    textPrimary   = FolioDark.textPrimary,
    textSecondary = FolioDark.textSecondary,
    textTertiary  = FolioDark.textTertiary,
    textOnAccent  = FolioDark.textOnAccent,

    border        = FolioDark.border,
    borderStrong  = FolioDark.borderStrong,
    borderSubtle  = FolioDark.borderSubtle,

    accentPrimary      = FolioDark.accentPrimary,
    accentPrimaryHover = FolioDark.accentPrimaryHover,
    accentPrimarySoft  = FolioDark.accentPrimarySoft,

    accentAi      = FolioDark.accentAi,
    accentAiHover = FolioDark.accentAiHover,
    accentAiSoft  = FolioDark.accentAiSoft,

    accentSaved      = FolioDark.accentSaved,
    accentSavedHover = FolioDark.accentSavedHover,
    accentSavedSoft  = FolioDark.accentSavedSoft,

    accentPremium      = FolioDark.accentPremium,
    accentPremiumHover = FolioDark.accentPremiumHover,
    accentPremiumSoft  = FolioDark.accentPremiumSoft,

    error     = FolioDark.error,
    errorSoft = FolioDark.errorSoft,
)
