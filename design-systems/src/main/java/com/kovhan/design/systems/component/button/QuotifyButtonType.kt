package com.kovhan.design.systems.component.button

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Visual style of the button. Decides how the accent color is applied:
 *
 * - [Filled]   — solid background, contrasting label (primary CTA)
 * - [Tonal]    — soft accent tint background, accent-colored label (secondary CTA)
 * - [Outlined] — transparent background with accent border + accent label
 * - [Ghost]    — no background, no border, accent label only (tertiary / inline)
 */
enum class QuotifyButtonVariant { Filled, Tonal, Outlined, Ghost }

/**
 * Size preset. Maps to height, padding, icon size and the label's text style.
 *
 * - [Small]  — 36 dp, caption text — inline actions, dense lists
 * - [Medium] — 44 dp, body text    — default form / dialog actions
 * - [Large]  — 52 dp, body strong  — page-level CTAs (login, onboarding "Далі")
 */
enum class QuotifyButtonSize { Small, Medium, Large }

/**
 * Semantic color role. Pulled from the Folio accent palette so swapping a
 * button's intent stays consistent in light and dark themes.
 *
 * - [Primary]     — Terracotta. Main app actions.
 * - [Ai]          — Blue. Anything that opens or invokes the AI assistant.
 * - [Saved]       — Olive. Library / collection / "saved" actions.
 * - [Premium]     — Gold. Featured / premium / paywall actions.
 * - [Neutral]     — Ink + paper. Cancel, dismiss, low-emphasis flows.
 * - [Destructive] — Error red. Delete, remove, sign out, etc.
 */
enum class QuotifyButtonAccent { Primary, Ai, Saved, Premium, Neutral, Destructive }

/**
 * Resolved colors for one button in one state. Build via
 * `QuotifyButtonDefaults.colors(variant, accent)` or construct directly to
 * override individual fields.
 */
@Immutable
data class QuotifyButtonColors(
    val container: Color,
    val content: Color,
    val border: Color,
    val disabledContainer: Color,
    val disabledContent: Color,
    val disabledBorder: Color,
)

/**
 * Resolved dimensions for one button size. Build via
 * `QuotifyButtonDefaults.sizeSpec(size)` or construct directly to override.
 */
@Immutable
data class QuotifyButtonSizeSpec(
    val height: Dp,
    val horizontalPadding: Dp,
    val iconSize: Dp,
    val iconGap: Dp,
    val borderWidth: Dp,
    val textStyle: TextStyle,
    val shape: Shape,
) {
    companion object {
        val DefaultBorderWidth: Dp = 1.dp
    }
}
