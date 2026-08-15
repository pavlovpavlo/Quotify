package com.kovhan.core.ui.mapper

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.kovhan.core.models.widget.WidgetCovers
import com.kovhan.design.systems.R as DsR

/**
 * Resolves cover ids to their bundled photo and average colour. The averages
 * were sampled when the assets were prepared — cheaper than decoding the bitmap
 * at render time, and identical between preview and widget.
 */
object WidgetCoverMapper {

    private data class Cover(
        @DrawableRes val imageRes: Int,
        val average: Color,
    )

    private val covers: Map<String, Cover> = mapOf(
        "cover_1" to Cover(DsR.drawable.img_widget_cover_1, Color(0xFF1B2837)),
        "cover_2" to Cover(DsR.drawable.img_widget_cover_2, Color(0xFF161A26)),
        "cover_3" to Cover(DsR.drawable.img_widget_cover_3, Color(0xFF4F492E)),
        "cover_4" to Cover(DsR.drawable.img_widget_cover_4, Color(0xFFC9B9A4)),
        "cover_5" to Cover(DsR.drawable.img_widget_cover_5, Color(0xFFC6AB90)),
        "cover_6" to Cover(DsR.drawable.img_widget_cover_6, Color(0xFFBB9B7C)),
        "cover_7" to Cover(DsR.drawable.img_widget_cover_7, Color(0xFF724C2D)),
        "cover_8" to Cover(DsR.drawable.img_widget_cover_8, Color(0xFF715635)),
        "cover_9" to Cover(DsR.drawable.img_widget_cover_9, Color(0xFF80684A)),
        "cover_10" to Cover(DsR.drawable.img_widget_cover_10, Color(0xFF685747)),
        "cover_11" to Cover(DsR.drawable.img_widget_cover_11, Color(0xFF0F1823)),
        "cover_12" to Cover(DsR.drawable.img_widget_cover_12, Color(0xFF4D535D)),
        "cover_13" to Cover(DsR.drawable.img_widget_cover_13, Color(0xFFB6B6B7)),
        "cover_14" to Cover(DsR.drawable.img_widget_cover_14, Color(0xFF938C80)),
        "cover_15" to Cover(DsR.drawable.img_widget_cover_15, Color(0xFF616553)),
    )

    /** Stand-in blurred photo shown for every cover while the effect is being reviewed. */
    private val blurred = Cover(DsR.drawable.img_widget_cover_blurred, Color(0xFFB4A08A))

    @DrawableRes
    fun imageRes(coverId: String, blurEnabled: Boolean): Int =
        if (blurEnabled) blurred.imageRes else cover(coverId).imageRes

    /** Average photo colour — the reference for readable text and the frame. */
    fun averageColor(coverId: String, blurEnabled: Boolean = false): Color =
        if (blurEnabled) blurred.average else cover(coverId).average

    private fun cover(coverId: String): Cover =
        covers[coverId] ?: covers.getValue(WidgetCovers.DEFAULT)
}
