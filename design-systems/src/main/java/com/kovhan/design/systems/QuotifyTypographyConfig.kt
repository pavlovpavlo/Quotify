package com.kovhan.design.systems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.util.getDimensionMultiplier

val LocalTypography = compositionLocalOf { QuotifyTypography() }

val QuotifyFontFamily = FontFamily(
    Font(R.font.jakarta_regular, FontWeight.W400),
    Font(R.font.jakarta_medium, FontWeight.W500),
    Font(R.font.jakarta_bold, FontWeight.W700),
    Font(R.font.jakarta_semibold, FontWeight.W600), // якщо є
)

@Composable
internal fun provideTypography(): QuotifyTypography {
    val multiplier = getDimensionMultiplier()

    val textDimension = defaultTextDimensions.copy(
        displayLarge = defaultTextDimensions.displayLarge * multiplier,
        display = defaultTextDimensions.display * multiplier,
        displaySmall = defaultTextDimensions.displaySmall * multiplier,
        headlineLarge = defaultTextDimensions.headlineLarge * multiplier,
        headlineBig = defaultTextDimensions.headlineBig * multiplier,
        headline = defaultTextDimensions.headline * multiplier,
        title = defaultTextDimensions.title * multiplier,
        subtitle = defaultTextDimensions.subtitle * multiplier,
        body = defaultTextDimensions.body * multiplier,
        label = defaultTextDimensions.label * multiplier,
        smallLabel = defaultTextDimensions.smallLabel * multiplier,
        tiny = defaultTextDimensions.tiny * multiplier,
    )

    return QuotifyTypography(
        displayLargeSemibold = defaultTypography.displayLargeSemibold.copy(fontSize = textDimension.displayLarge),
        displayBold = defaultTypography.displayBold.copy(fontSize = textDimension.display),
        displaySemibold = defaultTypography.displaySemibold.copy(fontSize = textDimension.display),
        displaySmallSemibold = defaultTypography.displaySmallSemibold.copy(fontSize = textDimension.displaySmall),
        headlineLargeNormal = defaultTypography.headlineLargeNormal.copy(fontSize = textDimension.headlineLarge),
        headlineLargeMedium = defaultTypography.headlineLargeMedium.copy(fontSize = textDimension.headlineLarge),
        headlineBigMedium = defaultTypography.headlineBigMedium.copy(fontSize = textDimension.headlineBig),
        headlineBigBold = defaultTypography.headlineBigBold.copy(fontSize = textDimension.headlineBig),
        headlineBold = defaultTypography.headlineBold.copy(fontSize = textDimension.headline),
        headlineSemibold = defaultTypography.headlineSemibold.copy(fontSize = textDimension.headline),
        headlineNormal = defaultTypography.headlineNormal.copy(fontSize = textDimension.headline),
        titleBold = defaultTypography.titleBold.copy(fontSize = textDimension.title),
        titleSemibold = defaultTypography.titleSemibold.copy(fontSize = textDimension.title),
        titleMedium = defaultTypography.titleMedium.copy(fontSize = textDimension.title),
        titleNormal = defaultTypography.titleNormal.copy(fontSize = textDimension.title),
        subtitleSemibold = defaultTypography.subtitleSemibold.copy(fontSize = textDimension.subtitle),
        subtitleMedium = defaultTypography.subtitleMedium.copy(fontSize = textDimension.subtitle),
        bodyBold = defaultTypography.bodyBold.copy(fontSize = textDimension.body),
        bodySemibold = defaultTypography.bodySemibold.copy(fontSize = textDimension.body),
        bodyNormal = defaultTypography.bodyNormal.copy(fontSize = textDimension.body),
        bodyMedium = defaultTypography.bodyMedium.copy(fontSize = textDimension.body),
        labelBold = defaultTypography.labelBold.copy(fontSize = textDimension.label),
        labelSemibold = defaultTypography.labelSemibold.copy(fontSize = textDimension.label),
        labelMedium = defaultTypography.labelMedium.copy(fontSize = textDimension.label),
        labelNormal = defaultTypography.labelNormal.copy(fontSize = textDimension.label),
        smallLabelBold = defaultTypography.smallLabelBold.copy(fontSize = textDimension.smallLabel),
        smallLabelSemibold = defaultTypography.smallLabelSemibold.copy(fontSize = textDimension.smallLabel),
        smallLabelMedium = defaultTypography.smallLabelMedium.copy(fontSize = textDimension.smallLabel),
        smallLabelNormal = defaultTypography.smallLabelNormal.copy(fontSize = textDimension.smallLabel),
        tinySemibold = defaultTypography.tinySemibold.copy(fontSize = textDimension.tiny),
        tinyMedium = defaultTypography.tinyMedium.copy(fontSize = textDimension.tiny),
    )
}

@Immutable
data class TextDimensions(
    val displayExtraLarge: TextUnit,
    val displayLarge: TextUnit,
    val display: TextUnit,
    val displaySmall: TextUnit,
    val headlineLarge: TextUnit,
    val headlineBig: TextUnit,
    val headline: TextUnit,
    val title: TextUnit,
    val subtitle: TextUnit,
    val body: TextUnit,
    val label: TextUnit,
    val smallLabel: TextUnit,
    val tiny: TextUnit,
    val faint: TextUnit
)

val defaultTextDimensions = TextDimensions(
    displayExtraLarge = 42.sp,
    displayLarge = 36.sp,
    display = 24.sp,
    displaySmall = 22.sp,
    headlineLarge = 20.sp,
    headlineBig = 18.sp,
    headline = 17.sp,
    title = 16.sp,
    subtitle = 15.sp,
    body = 14.sp,
    label = 13.sp,
    smallLabel = 12.sp,
    tiny = 11.sp,
    faint = 8.sp
)

data class QuotifyTypography(
    val displayLargeExtraBold: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.displayLarge,
        fontWeight = FontWeight.W800,
        fontFamily = QuotifyFontFamily
    ),
    val displayLargeSemibold: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.displayLarge,
        fontWeight = FontWeight.W600,
        fontFamily = QuotifyFontFamily
    ),
    val displayBold: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.display,
        fontWeight = FontWeight.W700,
        fontFamily = QuotifyFontFamily
    ),
    val displaySemibold: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.display,
        fontWeight = FontWeight.W600,
        fontFamily = QuotifyFontFamily
    ),
    val displaySmallSemibold: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.displaySmall,
        fontWeight = FontWeight.W600,
        fontFamily = QuotifyFontFamily
    ),
    val displaySmallMedium: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.displaySmall,
        fontWeight = FontWeight.W500,
        fontFamily = QuotifyFontFamily
    ),
    val displayNormal: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.display,
        fontWeight = FontWeight.W400,
        fontFamily = QuotifyFontFamily
    ),
    val headlineLargeNormal: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.headlineLarge,
        fontWeight = FontWeight.W400,
        fontFamily = QuotifyFontFamily
    ),
    val headlineLargeMedium: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.headlineLarge,
        fontWeight = FontWeight.W500,
        fontFamily = QuotifyFontFamily
    ),
    val headlineBigMedium: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.headlineBig,
        fontWeight = FontWeight.W500,
        fontFamily = QuotifyFontFamily
    ),
    val headlineBigBold: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.headlineBig,
        fontWeight = FontWeight.W700,
        fontFamily = QuotifyFontFamily
    ),
    val headlineBold: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.headline,
        fontWeight = FontWeight.W700,
        fontFamily = QuotifyFontFamily
    ),
    val headlineSemibold: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.headline,
        fontWeight = FontWeight.W600,
        fontFamily = QuotifyFontFamily
    ),
    val headlineNormal: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.headline,
        fontWeight = FontWeight.W400,
        fontFamily = QuotifyFontFamily
    ),
    val titleBold: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.title,
        fontWeight = FontWeight.W700,
        fontFamily = QuotifyFontFamily
    ),
    val titleSemibold: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.title,
        fontWeight = FontWeight.W600,
        fontFamily = QuotifyFontFamily
    ),
    val titleMedium: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.title,
        fontWeight = FontWeight.W500,
        fontFamily = QuotifyFontFamily
    ),
    val titleNormal: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.title,
        fontWeight = FontWeight.W400,
        fontFamily = QuotifyFontFamily
    ),
    val subtitleSemibold: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.subtitle,
        fontWeight = FontWeight.W600,
        fontFamily = QuotifyFontFamily
    ),
    val subtitleMedium: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.subtitle,
        fontWeight = FontWeight.W500,
        fontFamily = QuotifyFontFamily
    ),
    val subtitleNormal: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.subtitle,
        fontWeight = FontWeight.W400,
        fontFamily = QuotifyFontFamily
    ),
    val bodyBold: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.body,
        fontWeight = FontWeight.W700,
        fontFamily = QuotifyFontFamily
    ),
    val bodySemibold: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.body,
        fontWeight = FontWeight.W600,
        fontFamily = QuotifyFontFamily
    ),
    val bodyMedium: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.body,
        fontWeight = FontWeight.W500,
        fontFamily = QuotifyFontFamily
    ),
    val bodyNormal: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.body,
        fontWeight = FontWeight.W400,
        fontFamily = QuotifyFontFamily
    ),
    val labelBold: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.label,
        fontWeight = FontWeight.W700,
        fontFamily = QuotifyFontFamily
    ),
    val labelSemibold: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.label,
        fontWeight = FontWeight.W600,
        fontFamily = QuotifyFontFamily
    ),
    val labelMedium: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.label,
        fontWeight = FontWeight.W500,
        fontFamily = QuotifyFontFamily
    ),
    val labelNormal: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.label,
        fontWeight = FontWeight.W400,
        fontFamily = QuotifyFontFamily
    ),
    val smallLabelBold: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.smallLabel,
        fontWeight = FontWeight.W700,
        fontFamily = QuotifyFontFamily
    ),
    val smallLabelSemibold: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.smallLabel,
        fontWeight = FontWeight.W600,
        fontFamily = QuotifyFontFamily
    ),
    val smallLabelMedium: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.smallLabel,
        fontWeight = FontWeight.W500,
        fontFamily = QuotifyFontFamily
    ),
    val smallLabelNormal: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.smallLabel,
        fontWeight = FontWeight.W400,
        fontFamily = QuotifyFontFamily
    ),
    val tinySemibold: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.tiny,
        fontWeight = FontWeight.W600,
        fontFamily = QuotifyFontFamily
    ),
    val tinyMedium: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.tiny,
        fontWeight = FontWeight.W500,
        fontFamily = QuotifyFontFamily
    ),
    val tinyNormal: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.tiny,
        fontWeight = FontWeight.W400,
        fontFamily = QuotifyFontFamily
    ),
    val faintNormal: TextStyle = TextStyle(
        fontSize = defaultTextDimensions.faint,
        fontWeight = FontWeight.W400,
        fontFamily = QuotifyFontFamily
    )
)

val defaultTypography = QuotifyTypography()
