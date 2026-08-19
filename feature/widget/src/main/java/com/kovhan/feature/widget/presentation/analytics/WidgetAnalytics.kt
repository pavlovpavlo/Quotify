package com.kovhan.feature.widget.presentation.analytics

import com.kovhan.core.analytics.WidgetQuoteSource
import com.kovhan.core.analytics.WidgetStyleName
import com.kovhan.core.analytics.WidgetTextAlignment
import com.kovhan.core.analytics.WidgetTextColour
import com.kovhan.core.analytics.event.WidgetSnapshot
import com.kovhan.core.models.widget.WidgetBorderColor
import com.kovhan.core.models.widget.WidgetSettings
import com.kovhan.core.models.widget.WidgetSource
import com.kovhan.core.models.widget.WidgetStyle
import com.kovhan.core.models.widget.WidgetStyleSettings
import com.kovhan.core.models.widget.WidgetTextAlign
import com.kovhan.core.models.widget.WidgetTextColor

internal fun WidgetSettings.toSnapshot(): WidgetSnapshot {
    val styleSettings = appearance[style]
    return WidgetSnapshot(
        style = style.toAnalytics(),
        backgroundId = styleSettings.backgroundId(),
        backgroundColourId = (styleSettings as? WidgetStyleSettings.Classic)?.toneId.orEmpty(),
        backgroundBlur = (styleSettings as? WidgetStyleSettings.Cover)?.blurEnabled ?: false,
        border = styleSettings.borderEnabled,
        borderColourId = styleSettings.borderColor.toAnalytics(),
        fontSize = styleSettings.fontSize.ordinal + 1,
        textColour = styleSettings.textColor.toAnalytics(),
        textAlignment = styleSettings.textAlign.toAnalytics(),
        quoteSource = source.toAnalytics(),
        addDailyQuote = includeDailyQuote,
        refreshHours = frequencyHours,
    )
}

private fun WidgetStyleSettings.backgroundId(): String = when (this) {
    is WidgetStyleSettings.Cover -> coverId
    is WidgetStyleSettings.Classic -> toneId
    is WidgetStyleSettings.Minimal -> ""
}

private fun WidgetStyle.toAnalytics(): WidgetStyleName = when (this) {
    WidgetStyle.MINIMAL -> WidgetStyleName.MINIMAL
    WidgetStyle.CLASSIC -> WidgetStyleName.CLASSIC
    WidgetStyle.COVER -> WidgetStyleName.COVER
}

internal fun WidgetSource.toAnalytics(): WidgetQuoteSource = when (this) {
    WidgetSource.All -> WidgetQuoteSource.ALL
    WidgetSource.Favorites -> WidgetQuoteSource.FAVOURITES
    is WidgetSource.Playlist -> WidgetQuoteSource.CUSTOM
}

private fun WidgetBorderColor.toAnalytics(): String = when (this) {
    WidgetBorderColor.Auto -> "auto"
    is WidgetBorderColor.Tone -> toneId
}

private fun WidgetTextColor.toAnalytics(): WidgetTextColour = when (this) {
    WidgetTextColor.Light -> WidgetTextColour.WHITE
    WidgetTextColor.Dark -> WidgetTextColour.BLACK
    else -> WidgetTextColour.CUSTOM
}

private fun WidgetTextAlign.toAnalytics(): WidgetTextAlignment = when (this) {
    WidgetTextAlign.CENTER -> WidgetTextAlignment.CENTER
    WidgetTextAlign.START -> WidgetTextAlignment.LEFT
}
