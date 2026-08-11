package com.kovhan.feature.widget.glance

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontFamily
import androidx.glance.text.FontStyle
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.kovhan.core.models.widget.WidgetQuote
import com.kovhan.core.navigation.EXTRA_OPEN_WIDGET_QUOTE
import com.kovhan.core.navigation.EXTRA_OPEN_WIDGET_SETTINGS
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.widget.R

internal data class WidgetStrings(
    val emptyTitle: String,
    val emptyHint: String,
)

private val COMPACT_HEIGHT = 90.dp

@Composable
internal fun WidgetContent(
    quote: WidgetQuote?,
    strings: WidgetStrings,
) {
    val context = LocalContext.current
    val primaryIntent =
        if (quote != null) openQuoteIntent(context, quote.id) else openSettingsIntent(context)

    // Below two cells tall there is no room for the settings affordance next to
    // the text, so the chrome collapses and the padding tightens.
    val compact = LocalSize.current.height < COMPACT_HEIGHT

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .appWidgetBackground()
            .background(ColorProvider(R.color.widget_bg))
            .cornerRadius(16.dp)
            .clickable(actionStartActivity(primaryIntent)),
    ) {
        Box(modifier = GlanceModifier.fillMaxSize().padding(if (compact) 8.dp else 16.dp)) {
            if (quote == null) EmptyState(strings, compact) else QuoteState(quote, compact)
        }

        if (!compact) {
            Box(
                modifier = GlanceModifier.fillMaxSize().padding(8.dp),
                contentAlignment = Alignment.TopEnd,
            ) {
                Image(
                    provider = ImageProvider(DsR.drawable.ic_settings),
                    contentDescription = null,
                    modifier = GlanceModifier
                        .size(20.dp)
                        .clickable(actionStartActivity(openSettingsIntent(context))),
                    colorFilter = ColorFilter.tint(ColorProvider(R.color.widget_text_secondary)),
                )
            }
        }
    }
}

@Composable
private fun QuoteState(
    quote: WidgetQuote,
    compact: Boolean,
) {
    val meta = listOfNotNull(
        quote.authorName?.takeIf { it.isNotBlank() },
        quote.bookName?.takeIf { it.isNotBlank() },
    ).joinToString(" — ")

    val height = LocalSize.current.height
    val maxLines = when {
        height < 60.dp -> 1
        height < 90.dp -> 2
        height < 120.dp -> 3
        height < 190.dp -> 5
        else -> 9
    }

    Column(modifier = GlanceModifier.fillMaxSize()) {
        Text(
            text = quote.text,
            maxLines = maxLines,
            modifier = GlanceModifier.fillMaxWidth().defaultWeight(),
            style = TextStyle(
                color = ColorProvider(R.color.widget_text_primary),
                fontSize = if (compact) 14.sp else 16.sp,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Serif,
            ),
        )

        if (meta.isNotEmpty() && !compact) {
            Text(
                text = meta,
                maxLines = 1,
                modifier = GlanceModifier.fillMaxWidth(),
                style = TextStyle(
                    color = ColorProvider(R.color.widget_text_secondary),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                ),
            )
        }
    }
}

@Composable
private fun EmptyState(
    strings: WidgetStrings,
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
                color = ColorProvider(R.color.widget_text_primary),
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
                    color = ColorProvider(R.color.widget_text_secondary),
                    fontSize = 12.sp,
                ),
            )
        }
    }
}

private fun openSettingsIntent(context: Context): Intent =
    launchIntent(context).apply { putExtra(EXTRA_OPEN_WIDGET_SETTINGS, true) }

private fun openQuoteIntent(context: Context, quoteId: String): Intent =
    launchIntent(context).apply { putExtra(EXTRA_OPEN_WIDGET_QUOTE, quoteId) }

private fun launchIntent(context: Context): Intent =
    (context.packageManager.getLaunchIntentForPackage(context.packageName) ?: Intent()).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }
