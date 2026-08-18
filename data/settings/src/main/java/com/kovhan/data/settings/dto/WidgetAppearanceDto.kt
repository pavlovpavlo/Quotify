package com.kovhan.data.settings.dto

import kotlinx.serialization.Serializable

/**
 * Storage shape of the widget appearance — one JSON blob under a single
 * preference key. Every field is optional with a default so an older payload
 * (or one written before a new control existed) still decodes.
 */
@Serializable
internal data class WidgetAppearanceDto(
    val minimal: MinimalDto = MinimalDto(),
    val classic: ClassicDto = ClassicDto(),
    val cover: CoverDto = CoverDto(),
) {
    @Serializable
    data class MinimalDto(
        val border: BorderDto = BorderDto(),
        val text: TextDto = TextDto(),
    )

    @Serializable
    data class ClassicDto(
        val toneId: String? = null,
        val border: BorderDto = BorderDto(),
        val text: TextDto = TextDto(),
    )

    @Serializable
    data class CoverDto(
        val coverId: String? = null,
        val blurEnabled: Boolean = false,
        val border: BorderDto = BorderDto(),
        val text: TextDto = TextDto(),
    )

    @Serializable
    data class BorderDto(
        val enabled: Boolean = false,
        val toneId: String? = null,
    )

    @Serializable
    data class TextDto(
        val fontSize: String? = null,
        val colorKind: String? = null,
        val colorArgb: Int? = null,
        val align: String? = null,
    )
}
