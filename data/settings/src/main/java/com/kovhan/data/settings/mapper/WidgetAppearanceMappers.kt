package com.kovhan.data.settings.mapper

import com.kovhan.core.models.widget.WidgetAppearance
import com.kovhan.core.models.widget.WidgetBorderColor
import com.kovhan.core.models.widget.WidgetCovers
import com.kovhan.core.models.widget.WidgetFontSize
import com.kovhan.core.models.widget.WidgetStyleSettings
import com.kovhan.core.models.widget.WidgetTextAlign
import com.kovhan.core.models.widget.WidgetTextColor
import com.kovhan.core.models.widget.WidgetTones
import com.kovhan.data.settings.dto.WidgetAppearanceDto

internal fun WidgetAppearanceDto.toDomain() = WidgetAppearance(
    minimal = WidgetStyleSettings.Minimal(
        borderEnabled = minimal.border.enabled,
        borderColor = minimal.border.toBorderColor(),
        fontSize = minimal.text.fontSize.toFontSize(),
        textColor = minimal.text.toTextColor(),
        textAlign = minimal.text.align.toTextAlign(),
    ),
    classic = WidgetStyleSettings.Classic(
        toneId = classic.toneId?.takeIf { it in WidgetTones.ALL } ?: WidgetTones.DEFAULT,
        borderEnabled = classic.border.enabled,
        borderColor = classic.border.toBorderColor(),
        fontSize = classic.text.fontSize.toFontSize(),
        textColor = classic.text.toTextColor(),
        textAlign = classic.text.align.toTextAlign(),
    ),
    cover = WidgetStyleSettings.Cover(
        coverId = cover.coverId?.takeIf { it in WidgetCovers.ALL } ?: WidgetCovers.DEFAULT,
        blurEnabled = cover.blurEnabled,
        borderEnabled = cover.border.enabled,
        borderColor = cover.border.toBorderColor(),
        fontSize = cover.text.fontSize.toFontSize(),
        textColor = cover.text.toTextColor(),
        textAlign = cover.text.align.toTextAlign(),
    ),
)

internal fun WidgetAppearance.toDto() = WidgetAppearanceDto(
    minimal = WidgetAppearanceDto.MinimalDto(
        border = minimal.toBorderDto(),
        text = minimal.toTextDto(),
    ),
    classic = WidgetAppearanceDto.ClassicDto(
        toneId = classic.toneId,
        border = classic.toBorderDto(),
        text = classic.toTextDto(),
    ),
    cover = WidgetAppearanceDto.CoverDto(
        coverId = cover.coverId,
        blurEnabled = cover.blurEnabled,
        border = cover.toBorderDto(),
        text = cover.toTextDto(),
    ),
)

private fun WidgetStyleSettings.toBorderDto() = WidgetAppearanceDto.BorderDto(
    enabled = borderEnabled,
    toneId = (borderColor as? WidgetBorderColor.Tone)?.toneId,
)

private fun WidgetAppearanceDto.BorderDto.toBorderColor(): WidgetBorderColor =
    toneId?.takeIf { it in WidgetTones.ALL }
        ?.let(WidgetBorderColor::Tone)
        ?: WidgetBorderColor.Auto

private fun WidgetStyleSettings.toTextDto() = WidgetAppearanceDto.TextDto(
    fontSize = fontSize.name,
    colorKind = textColor.kind(),
    colorArgb = (textColor as? WidgetTextColor.Custom)?.argb,
    align = textAlign.name,
)

private fun WidgetTextColor.kind(): String = when (this) {
    WidgetTextColor.Auto -> COLOR_AUTO
    WidgetTextColor.Light -> COLOR_LIGHT
    WidgetTextColor.Dark -> COLOR_DARK
    is WidgetTextColor.Custom -> COLOR_CUSTOM
}

private fun WidgetAppearanceDto.TextDto.toTextColor(): WidgetTextColor = when (colorKind) {
    COLOR_LIGHT -> WidgetTextColor.Light
    COLOR_DARK -> WidgetTextColor.Dark
    COLOR_CUSTOM -> colorArgb?.let(WidgetTextColor::Custom) ?: WidgetTextColor.Auto
    else -> WidgetTextColor.Auto
}

private fun String?.toFontSize(): WidgetFontSize =
    this?.let { runCatching { WidgetFontSize.valueOf(it) }.getOrNull() } ?: WidgetFontSize.DEFAULT

private fun String?.toTextAlign(): WidgetTextAlign =
    this?.let { runCatching { WidgetTextAlign.valueOf(it) }.getOrNull() } ?: WidgetTextAlign.DEFAULT

private const val COLOR_AUTO = "auto"
private const val COLOR_LIGHT = "light"
private const val COLOR_DARK = "dark"
private const val COLOR_CUSTOM = "custom"
