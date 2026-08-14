package com.kovhan.design.systems

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val LocaleQuotifyColors = compositionLocalOf { quotifyLightPalette }

/**
 * Premium surface roles — grouped because they only make sense together, on the
 * olive gradient card and CTA. Identical in light and dark: see [FolioPremium].
 */
@Immutable
data class QuotifyPremiumColors(
    val gradient: Brush,
    val onGradient: Color,
    val onGradientMuted: Color,
    val onGradientSubtle: Color,
    val card: Color,
    val cardDivider: Color,
    val cardText: Color,
    val iconBackground: Color,
    val iconTint: Color,
)

/**
 * Special-offer roles. [heroGradient] is fixed in both themes (see [FolioOffer]);
 * the rest follow the theme, so this group is built per palette.
 */
@Immutable
data class QuotifyOfferColors(
    val heroGradient: Brush,
    val ctaGradient: Brush,
    val closeScrim: Color,
    val closeIcon: Color,
)

private val offerHeroGradient = Gradients.offerHero(
    start = FolioOffer.heroGradientStart,
    end = FolioOffer.heroGradientEnd,
)

val quotifyLightOfferColors = QuotifyOfferColors(
    heroGradient = offerHeroGradient,
    ctaGradient = Gradients.offerCta(FolioLight.accentPrimary, FolioOffer.ctaGradientEnd),
    closeScrim = FolioOffer.closeScrimLight,
    closeIcon = FolioOffer.closeIconLight,
)

val quotifyDarkOfferColors = QuotifyOfferColors(
    heroGradient = offerHeroGradient,
    ctaGradient = Gradients.offerCta(FolioDark.accentPrimary, FolioOffer.ctaGradientEnd),
    closeScrim = FolioOffer.closeScrimDark,
    closeIcon = FolioOffer.closeIconDark,
)

val quotifyPremiumColors = QuotifyPremiumColors(
    gradient = Gradients.premium(
        start = FolioPremium.gradientStart,
        middle = FolioPremium.gradientMiddle,
        end = FolioPremium.gradientEnd,
    ),
    onGradient       = FolioPremium.onGradient,
    onGradientMuted  = FolioPremium.onGradientMuted,
    onGradientSubtle = FolioPremium.onGradientSubtle,

    card           = FolioPremium.card,
    cardDivider    = FolioPremium.cardDivider,
    cardText       = FolioPremium.cardText,
    iconBackground = FolioPremium.iconBackground,
    iconTint       = FolioPremium.iconTint,
)

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

    // Accent — Plum (fixed hex, shared light/dark)
    val accentPlum: Color,

    // Accent — Teal (fixed hex, shared light/dark)
    val accentTeal: Color,

    // Premium surfaces (fixed hex, shared light/dark)
    val premium: QuotifyPremiumColors,

    // Special-offer screen
    val offer: QuotifyOfferColors,

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

    accentPlum = FolioLight.accentPlum,
    accentTeal = FolioLight.accentTeal,

    premium = quotifyPremiumColors,
    offer   = quotifyLightOfferColors,

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

    accentPlum = FolioDark.accentPlum,
    accentTeal = FolioDark.accentTeal,

    premium = quotifyPremiumColors,
    offer   = quotifyDarkOfferColors,

    error     = FolioDark.error,
    errorSoft = FolioDark.errorSoft,
)
