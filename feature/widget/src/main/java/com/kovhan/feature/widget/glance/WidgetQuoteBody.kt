package com.kovhan.feature.widget.glance

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.ColumnScope
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.text.FontFamily
import androidx.glance.text.FontStyle
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.kovhan.core.models.widget.WidgetQuote
import com.kovhan.core.models.widget.WidgetStyleSettings
import com.kovhan.core.models.widget.WidgetTextAlign

@Composable
internal fun ColumnScope.WidgetQuoteBody(
    quote: WidgetQuote,
    settings: WidgetStyleSettings,
    textColor: Color,
    metrics: WidgetLayoutMetrics,
) {
    val meta = listOfNotNull(
        quote.authorName?.takeIf { it.isNotBlank() },
        quote.bookName?.takeIf { it.isNotBlank() },
    ).joinToString(" — ")

    val showMeta = meta.isNotEmpty() && !metrics.compact
    val quoteSize = metrics.quoteFontSize(settings)
    val textAlign = settings.glanceTextAlign()

    Text(
        text = quote.text,
        maxLines = metrics.quoteMaxLines(quoteSize, showMeta),
        modifier = GlanceModifier.fillMaxWidth().defaultWeight(),
        style = TextStyle(
            color = ColorProvider(textColor),
            fontSize = quoteSize.sp,
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily.Serif,
            textAlign = textAlign,
        ),
    )

    if (showMeta) {
        Spacer(GlanceModifier.height(META_SPACING))
        Text(
            text = meta,
            maxLines = 1,
            modifier = GlanceModifier.fillMaxWidth(),
            style = TextStyle(
                color = ColorProvider(textColor.copy(alpha = META_ALPHA)),
                fontSize = META_FONT_SIZE.sp,
                fontWeight = FontWeight.Medium,
                textAlign = textAlign,
            ),
        )
    }
}

@Composable
internal fun WidgetEmptyBody(
    strings: WidgetStrings,
    textColor: Color,
    compact: Boolean,
) {
    Column(
        modifier = GlanceModifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = strings.emptyTitle,
            maxLines = 1,
            style = TextStyle(
                color = ColorProvider(textColor),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            ),
        )
        if (!compact) {
            Spacer(GlanceModifier.height(4.dp))
            Text(
                text = strings.emptyHint,
                maxLines = 2,
                style = TextStyle(
                    color = ColorProvider(textColor.copy(alpha = META_ALPHA)),
                    fontSize = META_FONT_SIZE.sp,
                ),
            )
        }
    }
}

/**
 * Height budget of one widget render.
 *
 * Glance cannot ellipsise on overflow — a `TextView` only shows the trailing
 * ellipsis when `maxLines` is what cuts the text off. Asking for more lines than
 * actually fit means the layout clips them instead, with no ellipsis, so the
 * line count has to be derived from the space really left for the quote.
 */
internal data class WidgetLayoutMetrics(
    val available: Dp,
    val compact: Boolean,
    val fontScale: Float,
) {
    fun quoteFontSize(settings: WidgetStyleSettings): Float =
        (if (compact) COMPACT_QUOTE_SIZE else BASE_QUOTE_SIZE) * settings.fontSize.scale

    /**
     * Font sizes are in sp, so the budget has to be measured in scaled units too —
     * a dp estimate silently misses the user's font-size setting entirely.
     */
    fun quoteMaxLines(quoteSize: Float, showMeta: Boolean): Int {
        val metaBlock = if (showMeta) {
            META_SPACING.value + META_FONT_SIZE * fontScale * META_LINE_RATIO
        } else {
            0f
        }
        val chrome = (if (compact) 0f else ICON_ROW_HEIGHT.value) + metaBlock
        val forQuote = available.value - chrome
        val lineHeight = quoteSize * fontScale * QUOTE_LINE_RATIO
        val fits = forQuote / lineHeight + LINE_ROUNDING
        val minLines = if (compact) 1 else MIN_QUOTE_LINES
        return fits.toInt().coerceIn(minLines, MAX_QUOTE_LINES)
    }

    companion object {
        @Composable
        fun of(compact: Boolean, padding: Dp): WidgetLayoutMetrics = WidgetLayoutMetrics(
            available = LocalSize.current.height - padding * 2,
            compact = compact,
            fontScale = LocalContext.current.resources.configuration.fontScale,
        )
    }
}

private fun WidgetStyleSettings.glanceTextAlign(): TextAlign = when (textAlign) {
    WidgetTextAlign.START -> TextAlign.Start
    WidgetTextAlign.CENTER -> TextAlign.Center
}

private const val BASE_QUOTE_SIZE = 16f
private const val COMPACT_QUOTE_SIZE = 14f
private const val META_FONT_SIZE = 12f
private const val META_ALPHA = 0.82f
private const val QUOTE_LINE_RATIO = 1.35f
private const val META_LINE_RATIO = 1.35f
private const val LINE_ROUNDING = 0.15f
private const val MIN_QUOTE_LINES = 2
private const val MAX_QUOTE_LINES = 9
private val META_SPACING = 4.dp
private val ICON_ROW_HEIGHT = 16.dp
