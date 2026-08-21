package com.kovhan.core.ui.mapper

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.kovhan.core.models.widget.WidgetCovers
import com.kovhan.core.ui.mapper.WidgetCoverMapper.blurred
import kotlin.collections.Map
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

    /** Reward photos unlocked one by one as surveys are finished. */
    private val specialCovers: Map<String, Cover> = mapOf(
        "special_1" to Cover(DsR.drawable.img_widget_cover_special_1, Color(0xFFA0A53A)),
        "special_2" to Cover(DsR.drawable.img_widget_cover_special_2, Color(0xFF554E43)),
        "special_3" to Cover(DsR.drawable.img_widget_cover_special_3, Color(0xFF43352B)),
        "special_4" to Cover(DsR.drawable.img_widget_cover_special_4, Color(0xFF221F1B)),
        "special_5" to Cover(DsR.drawable.img_widget_cover_special_5, Color(0xFF141314)),
        "special_6" to Cover(DsR.drawable.img_widget_cover_special_6, Color(0xFF525252)),
        "special_7" to Cover(DsR.drawable.img_widget_cover_special_7, Color(0xFF060E1C)),
        "special_8" to Cover(DsR.drawable.img_widget_cover_special_8, Color(0xFF1A3864)),
        "special_9" to Cover(DsR.drawable.img_widget_cover_special_9, Color(0xFF5E523C)),
        "special_10" to Cover(DsR.drawable.img_widget_cover_special_10, Color(0xFF2D333B)),
    )

    /** Stand-in blurred photo shown for every cover while the effect is being reviewed. */
    private val blurred: Map<String, Cover> = mapOf(
        "cover_1" to Cover(DsR.drawable.img_widget_cover_1_blurred, Color(0xFF1B2837)),
        "cover_2" to Cover(DsR.drawable.img_widget_cover_2_blurred, Color(0xFF161A26)),
        "cover_3" to Cover(DsR.drawable.img_widget_cover_3_blurred, Color(0xFF4F492E)),
        "cover_4" to Cover(DsR.drawable.img_widget_cover_4_blurred, Color(0xFFC9B9A4)),
        "cover_5" to Cover(DsR.drawable.img_widget_cover_5_blurred, Color(0xFFC6AB90)),
        "cover_6" to Cover(DsR.drawable.img_widget_cover_6_blurred, Color(0xFFBB9B7C)),
        "cover_7" to Cover(DsR.drawable.img_widget_cover_7_blurred, Color(0xFF724C2D)),
        "cover_8" to Cover(DsR.drawable.img_widget_cover_8_blurred, Color(0xFF715635)),
        "cover_9" to Cover(DsR.drawable.img_widget_cover_9_blurred, Color(0xFF80684A)),
        "cover_10" to Cover(DsR.drawable.img_widget_cover_10_blurred, Color(0xFF685747)),
        "cover_11" to Cover(DsR.drawable.img_widget_cover_11_blurred, Color(0xFF0F1823)),
        "cover_12" to Cover(DsR.drawable.img_widget_cover_12_blurred, Color(0xFF4D535D)),
        "cover_13" to Cover(DsR.drawable.img_widget_cover_13_blurred, Color(0xFFB6B6B7)),
        "cover_14" to Cover(DsR.drawable.img_widget_cover_14_blurred, Color(0xFF938C80)),
        "cover_15" to Cover(DsR.drawable.img_widget_cover_15_blurred, Color(0xFF616553)),
    )

    private val specialBlurred: Map<String, Cover> = mapOf(
        "special_1" to Cover(DsR.drawable.img_widget_cover_special_1_blurred, Color(0xFFA0A53A)),
        "special_2" to Cover(DsR.drawable.img_widget_cover_special_2_blurred, Color(0xFF554E43)),
        "special_3" to Cover(DsR.drawable.img_widget_cover_special_3_blurred, Color(0xFF43352B)),
        "special_4" to Cover(DsR.drawable.img_widget_cover_special_4_blurred, Color(0xFF221F1B)),
        "special_5" to Cover(DsR.drawable.img_widget_cover_special_5_blurred, Color(0xFF141314)),
        "special_6" to Cover(DsR.drawable.img_widget_cover_special_6_blurred, Color(0xFF525252)),
        "special_7" to Cover(DsR.drawable.img_widget_cover_special_7_blurred, Color(0xFF060E1C)),
        "special_8" to Cover(DsR.drawable.img_widget_cover_special_8_blurred, Color(0xFF1A3864)),
        "special_9" to Cover(DsR.drawable.img_widget_cover_special_9_blurred, Color(0xFF5E523C)),
        "special_10" to Cover(DsR.drawable.img_widget_cover_special_10_blurred, Color(0xFF2D333B)),
    )
    @DrawableRes
    fun imageRes(coverId: String, blurEnabled: Boolean): Int =
        if (blurEnabled) blurred(coverId).imageRes else cover(coverId).imageRes

    /** Average photo colour — the reference for readable text and the frame. */
    fun averageColor(coverId: String, blurEnabled: Boolean = false): Color =
        if (blurEnabled) blurred(coverId).average else cover(coverId).average

    private fun cover(coverId: String): Cover =
        covers[coverId] ?: specialCovers[coverId] ?: covers.getValue(WidgetCovers.DEFAULT)

    private fun blurred(coverId: String): Cover =
        blurred[coverId] ?: specialBlurred[coverId] ?: blurred.getValue(WidgetCovers.DEFAULT)
}
