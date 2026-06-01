package com.kovhan.design.systems.component.button

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.QuotifyColorPalette
import com.kovhan.design.systems.QuotifyMaterialTheme

/**
 * Folio-spec defaults for [QuotifyButtonColors] and [QuotifyButtonSizeSpec].
 *
 * Compose any button through this object so the visual ladder
 * (variant × accent × size) stays consistent across the app.
 *
 *   QuotifyButton(text = "Save", variant = Tonal, accent = Saved, size = Medium)
 *
 * To deviate without abandoning the system, copy a resolved value:
 *
 *   val colors = QuotifyButtonDefaults
 *       .colors(Filled, Primary)
 *       .copy(container = QuotifyMaterialTheme.colors.bgElevated)
 *
 */
object QuotifyButtonDefaults {

    /**
     * Container / content / border colors for the given combination.
     *
     * Disabled colors fall back to muted neutrals so a disabled button looks
     * the same no matter which accent it normally uses — keeps the UI calm.
     */
    @Composable
    fun colors(
        variant: QuotifyButtonVariant = QuotifyButtonVariant.Filled,
        accent: QuotifyButtonAccent = QuotifyButtonAccent.Primary,
    ): QuotifyButtonColors {
        val palette = QuotifyMaterialTheme.colors
        val tone = palette.accentTone(accent)

        // Ghost + Neutral is the "Skip / Cancel" pattern — low-emphasis muted
        // text. Bump content down to textSecondary so it doesn't compete with
        // primary headlines.
        val ghostNeutralContent = palette.textSecondary

        val (container, content, border) = when (variant) {
            QuotifyButtonVariant.Filled -> Triple(
                tone.base,
                tone.onAccent,
                Color.Transparent,
            )
            QuotifyButtonVariant.Tonal -> Triple(
                tone.soft,
                tone.base,
                Color.Transparent,
            )
            QuotifyButtonVariant.Outlined -> Triple(
                Color.Transparent,
                tone.base,
                tone.base,
            )
            QuotifyButtonVariant.Ghost -> Triple(
                Color.Transparent,
                if (accent == QuotifyButtonAccent.Neutral) ghostNeutralContent else tone.base,
                Color.Transparent,
            )
        }

        return QuotifyButtonColors(
            container = container,
            content = content,
            border = border,
            disabledContainer = when (variant) {
                QuotifyButtonVariant.Filled -> palette.bgSecondary
                QuotifyButtonVariant.Tonal -> palette.bgSecondary
                else -> Color.Transparent
            },
            disabledContent = palette.textTertiary,
            disabledBorder = when (variant) {
                QuotifyButtonVariant.Outlined -> palette.borderStrong
                else -> Color.Transparent
            },
        )
    }

    /**
     * Folio "pill" CTA — Large size, custom [height], full-radius shape.
     * Used by every auth screen and the post-onboarding Complete screen.
     */
    @Composable
    fun pillSizeSpec(height: Dp = 50.dp): QuotifyButtonSizeSpec =
        sizeSpec(QuotifyButtonSize.Large).copy(
            height = height,
            shape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusFull),
        )

    @Composable
    fun sizeSpec(size: QuotifyButtonSize = QuotifyButtonSize.Large): QuotifyButtonSizeSpec {
        val dimensions = QuotifyMaterialTheme.dimensions
        val typography = QuotifyMaterialTheme.typography

        return when (size) {
            QuotifyButtonSize.Small -> QuotifyButtonSizeSpec(
                height = 36.dp,
                horizontalPadding = dimensions.space3,
                iconSize = dimensions.iconSm,
                iconGap = dimensions.space2,
                borderWidth = QuotifyButtonSizeSpec.DefaultBorderWidth,
                textStyle = typography.caption,
                shape = RoundedCornerShape(dimensions.radiusLg),
            )
            QuotifyButtonSize.Medium -> QuotifyButtonSizeSpec(
                height = 44.dp,
                horizontalPadding = dimensions.space5,
                iconSize = dimensions.iconMd,
                iconGap = dimensions.space2,
                borderWidth = QuotifyButtonSizeSpec.DefaultBorderWidth,
                textStyle = typography.body,
                shape = RoundedCornerShape(dimensions.radiusXl),
            )
            QuotifyButtonSize.Large -> QuotifyButtonSizeSpec(
                height = dimensions.buttonHeight,
                horizontalPadding = dimensions.space6,
                iconSize = dimensions.iconMd,
                iconGap = dimensions.space3,
                borderWidth = QuotifyButtonSizeSpec.DefaultBorderWidth,
                textStyle = typography.bodyStrong,
                shape = RoundedCornerShape(dimensions.radius2xl),
            )
        }
    }
}

/** Container/content/border triple resolved from an [accent] role. */
internal data class AccentTone(
    val base: Color,
    val hover: Color,
    val soft: Color,
    val onAccent: Color,
)

internal fun QuotifyColorPalette.accentTone(accent: QuotifyButtonAccent): AccentTone =
    when (accent) {
        QuotifyButtonAccent.Primary -> AccentTone(
            base = accentPrimary,
            hover = accentPrimaryHover,
            soft = accentPrimarySoft,
            onAccent = textOnAccent,
        )
        QuotifyButtonAccent.Ai -> AccentTone(
            base = accentAi,
            hover = accentAiHover,
            soft = accentAiSoft,
            onAccent = textOnAccent,
        )
        QuotifyButtonAccent.Saved -> AccentTone(
            base = accentSaved,
            hover = accentSavedHover,
            soft = accentSavedSoft,
            onAccent = textOnAccent,
        )
        QuotifyButtonAccent.Premium -> AccentTone(
            base = accentPremium,
            hover = accentPremiumHover,
            soft = accentPremiumSoft,
            onAccent = textOnAccent,
        )
        QuotifyButtonAccent.Neutral -> AccentTone(
            base = textPrimary,
            hover = textPrimary,
            soft = bgSecondary,
            onAccent = bgPrimary,
        )
        QuotifyButtonAccent.Destructive -> AccentTone(
            base = error,
            hover = error,
            soft = errorSoft,
            onAccent = textOnAccent,
        )
    }

