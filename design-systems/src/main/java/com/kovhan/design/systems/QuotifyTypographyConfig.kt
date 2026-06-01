package com.kovhan.design.systems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.util.getDimensionMultiplier

val LocalTypography = compositionLocalOf { QuotifyTypography() }

/* ------------------------------------------------------------------
 * Font families — Folio spec, all static optical-size sets:
 *   Newsreader 24pt (serif, headings + reading)
 *   Inter 18pt      (sans, UI body + labels)
 *   JetBrains Mono  (mono, code)
 * ------------------------------------------------------------------ */
val NewsreaderFamily: FontFamily = FontFamily(
    Font(R.font.newsreader_regular, FontWeight.W400),
    Font(R.font.newsreader_medium, FontWeight.W500),
    Font(R.font.newsreader_semibold, FontWeight.W600),
    Font(R.font.newsreader_bold, FontWeight.W700),
    Font(R.font.newsreader_italic, FontWeight.W400, FontStyle.Italic),
    Font(R.font.newsreader_medium_italic, FontWeight.W500, FontStyle.Italic),
)

val InterFamily: FontFamily = FontFamily(
    Font(R.font.inter_regular, FontWeight.W400),
    Font(R.font.inter_medium, FontWeight.W500),
    Font(R.font.inter_semibold, FontWeight.W600),
    Font(R.font.inter_bold, FontWeight.W700),
)

val JetBrainsMonoFamily: FontFamily = FontFamily(
    Font(R.font.jetbrains_mono_regular, FontWeight.W400),
    Font(R.font.jetbrains_mono_medium, FontWeight.W500),
)

/* Folio modular type scale (in sp). */
@Immutable
data class TextDimensions(
    val text3xs: TextUnit,
    val text2xs: TextUnit,
    val textXs:  TextUnit,
    val textSm:  TextUnit,
    val textBase: TextUnit,
    val textMd:  TextUnit,
    val textLg:  TextUnit,
    val textXl:  TextUnit,
    val text2xl: TextUnit,
    val text3xl: TextUnit,
    val text4xl: TextUnit,
    val text5xl: TextUnit,
    val text6xl: TextUnit,
)

val defaultTextDimensions = TextDimensions(
    text3xs = 11.sp,
    text2xs = 12.sp,
    textXs  = 13.sp,
    textSm  = 14.sp,
    textBase = 16.sp,
    textMd  = 17.sp,
    textLg  = 19.sp,
    textXl  = 22.sp,
    text2xl = 26.sp,
    text3xl = 32.sp,
    text4xl = 40.sp,
    text5xl = 52.sp,
    text6xl = 68.sp,
)

@Composable
internal fun provideTypography(): QuotifyTypography {
    val multiplier = getDimensionMultiplier()
    val s = defaultTextDimensions.copy(
        text3xs = defaultTextDimensions.text3xs * multiplier,
        text2xs = defaultTextDimensions.text2xs * multiplier,
        textXs  = defaultTextDimensions.textXs  * multiplier,
        textSm  = defaultTextDimensions.textSm  * multiplier,
        textBase = defaultTextDimensions.textBase * multiplier,
        textMd  = defaultTextDimensions.textMd  * multiplier,
        textLg  = defaultTextDimensions.textLg  * multiplier,
        textXl  = defaultTextDimensions.textXl  * multiplier,
        text2xl = defaultTextDimensions.text2xl * multiplier,
        text3xl = defaultTextDimensions.text3xl * multiplier,
        text4xl = defaultTextDimensions.text4xl * multiplier,
        text5xl = defaultTextDimensions.text5xl * multiplier,
        text6xl = defaultTextDimensions.text6xl * multiplier,
    )

    val d = defaultTypography
    return QuotifyTypography(
        display       = d.display.copy(fontSize = s.text6xl),
        h1            = d.h1.copy(fontSize = s.text4xl),
        h2            = d.h2.copy(fontSize = s.text3xl),
        h3            = d.h3.copy(fontSize = s.text2xl),
        h4            = d.h4.copy(fontSize = s.textLg),
        body          = d.body.copy(fontSize = s.textBase),
        bodyStrong    = d.bodyStrong.copy(fontSize = s.textBase),
        readingBody   = d.readingBody.copy(fontSize = s.textMd),
        lede          = d.lede.copy(fontSize = s.textXl),
        eyebrow       = d.eyebrow.copy(fontSize = s.text2xs),
        caption       = d.caption.copy(fontSize = s.textXs),
        meta          = d.meta.copy(fontSize = s.text2xs),
        small         = d.small.copy(fontSize = s.textXs),
        code          = d.code.copy(fontSize = s.textSm),
        blockquote    = d.blockquote.copy(fontSize = s.textLg),
    )
}

/**
 * Folio semantic text styles. Color is intentionally NOT baked in —
 * pass it at the call site so light/dark switching keeps working.
 */
@Immutable
data class QuotifyTypography(
    // Display & headlines — serif (Newsreader)
    val display: TextStyle = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = defaultTextDimensions.text6xl,
        fontWeight = FontWeight.W600,
        lineHeight = 1.15.em,
        letterSpacing = (-0.04).em,
    ),
    val h1: TextStyle = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = defaultTextDimensions.text4xl,
        fontWeight = FontWeight.W600,
        lineHeight = 1.15.em,
        letterSpacing = (-0.02).em,
    ),
    val h2: TextStyle = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = defaultTextDimensions.text3xl,
        fontWeight = FontWeight.W600,
        lineHeight = 1.3.em,
        letterSpacing = (-0.02).em,
    ),
    val h3: TextStyle = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = defaultTextDimensions.text2xl,
        fontWeight = FontWeight.W600,
        lineHeight = 1.3.em,
    ),
    // h4 — sans (Inter), used for UI section titles
    val h4: TextStyle = TextStyle(
        fontFamily = InterFamily,
        fontSize = defaultTextDimensions.textLg,
        fontWeight = FontWeight.W600,
        lineHeight = 1.3.em,
    ),
    // Body — sans (Inter), default UI body
    val body: TextStyle = TextStyle(
        fontFamily = InterFamily,
        fontSize = defaultTextDimensions.textBase,
        fontWeight = FontWeight.W400,
        lineHeight = 1.5.em,
    ),
    // Body strong — used on buttons, CTAs
    val bodyStrong: TextStyle = TextStyle(
        fontFamily = InterFamily,
        fontSize = defaultTextDimensions.textBase,
        fontWeight = FontWeight.W600,
        lineHeight = 1.5.em,
    ),
    // Reading body — serif, relaxed leading. For long-form quote/article reading.
    val readingBody: TextStyle = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = defaultTextDimensions.textMd,
        fontWeight = FontWeight.W400,
        lineHeight = 1.65.em,
    ),
    // Lede — italic serif intro paragraph
    val lede: TextStyle = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = defaultTextDimensions.textXl,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.W400,
        lineHeight = 1.3.em,
    ),
    // Eyebrow — small uppercase label above headings
    val eyebrow: TextStyle = TextStyle(
        fontFamily = InterFamily,
        fontSize = defaultTextDimensions.text2xs,
        fontWeight = FontWeight.W600,
        letterSpacing = 0.12.em,
    ),
    val caption: TextStyle = TextStyle(
        fontFamily = InterFamily,
        fontSize = defaultTextDimensions.textXs,
        fontWeight = FontWeight.W400,
        lineHeight = 1.5.em,
    ),
    val meta: TextStyle = TextStyle(
        fontFamily = InterFamily,
        fontSize = defaultTextDimensions.text2xs,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.04.em,
    ),
    val small: TextStyle = TextStyle(
        fontFamily = InterFamily,
        fontSize = defaultTextDimensions.textXs,
        fontWeight = FontWeight.W400,
        lineHeight = 1.5.em,
    ),
    val code: TextStyle = TextStyle(
        fontFamily = JetBrainsMonoFamily,
        fontSize = defaultTextDimensions.textSm,
        fontWeight = FontWeight.W400,
    ),
    val blockquote: TextStyle = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = defaultTextDimensions.textLg,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.W400,
        lineHeight = 1.65.em,
    ),
)

val defaultTypography = QuotifyTypography()

