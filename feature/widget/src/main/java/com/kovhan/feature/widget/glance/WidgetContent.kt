package com.kovhan.feature.widget.glance

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
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
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.unit.ColorProvider
import com.kovhan.core.models.widget.WidgetQuote
import com.kovhan.core.models.widget.WidgetStyleSettings
import com.kovhan.core.navigation.EXTRA_OPEN_WIDGET_QUOTE
import com.kovhan.core.navigation.EXTRA_OPEN_WIDGET_SETTINGS
import com.kovhan.core.ui.mapper.WidgetBackgroundMapper
import com.kovhan.core.ui.mapper.WidgetCoverMapper
import com.kovhan.feature.widget.R
import com.kovhan.design.systems.R as DsR

internal data class WidgetStrings(
    val emptyTitle: String,
    val emptyHint: String,
)

// The default 4x2 placement reports well under 90dp of usable height on most
// launchers, so a higher threshold hid the icon and the author line at the size
// the widget actually ships at. Only a deliberately shrunken widget collapses.
private val COMPACT_HEIGHT = 56.dp
private val CORNER_RADIUS = 16.dp
private val CONTENT_PADDING = 12.dp
private val COMPACT_PADDING = 8.dp
private val SETTINGS_ICON_SIZE = 16.dp

@Composable
internal fun WidgetContent(
    quote: WidgetQuote?,
    settings: WidgetStyleSettings,
    strings: WidgetStrings,
) {
    val context = LocalContext.current
    val primaryIntent = when (quote) {
        null -> openSettingsIntent(context)
        else -> openQuoteIntent(context, quote.id)
    }

    val size = LocalSize.current
    val compact = size.height < COMPACT_HEIGHT
    val sizeScale = WidgetLayoutMetrics.scaleFor(size)
    val padding = if (compact) COMPACT_PADDING else CONTENT_PADDING * sizeScale

    val darkTheme = context.isDarkTheme()
    val fill = WidgetBackgroundMapper.fillColor(settings)
    val border = WidgetBackgroundMapper.borderColor(settings, darkTheme)
    val text = WidgetBackgroundMapper.textColor(settings, darkTheme)
    val metrics = WidgetLayoutMetrics.of(
        compact = compact,
        padding = padding,
        sizeScale = sizeScale,
        size = size,
    )

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .appWidgetBackground()
            .cornerRadius(CORNER_RADIUS)
            .background(ColorProvider(fill))
            .clickable(actionStartActivity(primaryIntent)),
    ) {
        if (settings is WidgetStyleSettings.Cover) {
            Image(
                provider = ImageProvider(
                    WidgetCoverMapper.imageRes(settings.coverId, settings.blurEnabled),
                ),
                contentDescription = null,
                modifier = GlanceModifier.fillMaxSize().cornerRadius(CORNER_RADIUS),
                contentScale = ContentScale.Crop,
            )
        }

        // A stroked drawable rather than a padded box behind the content: the
        // hollow centre keeps a transparent style transparent, and the stroke
        // stays the same width on every edge at any widget size.
        if (border != null) {
            Image(
                provider = ImageProvider(R.drawable.widget_border),
                contentDescription = null,
                modifier = GlanceModifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                colorFilter = ColorFilter.tint(ColorProvider(border)),
            )
        }

        Column(modifier = GlanceModifier.fillMaxSize().padding(padding)) {
            if (!compact) {
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                ) {
                    Image(
                        provider = ImageProvider(DsR.drawable.ic_settings),
                        contentDescription = null,
                        modifier = GlanceModifier
                            .size(SETTINGS_ICON_SIZE * sizeScale)
                            .clickable(actionStartActivity(openSettingsIntent(context))),
                        colorFilter = ColorFilter.tint(ColorProvider(text)),
                    )
                }
            }

            if (quote == null) {
                WidgetEmptyBody(strings, text, metrics)
            } else {
                WidgetQuoteBody(quote, settings, text, metrics)
            }
        }
    }
}

private fun Context.isDarkTheme(): Boolean =
    (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
        Configuration.UI_MODE_NIGHT_YES

private fun openSettingsIntent(context: Context): Intent =
    launchIntent(context).apply { putExtra(EXTRA_OPEN_WIDGET_SETTINGS, true) }

private fun openQuoteIntent(context: Context, quoteId: String): Intent =
    launchIntent(context).apply { putExtra(EXTRA_OPEN_WIDGET_QUOTE, quoteId) }

private fun launchIntent(context: Context): Intent =
    (context.packageManager.getLaunchIntentForPackage(context.packageName) ?: Intent()).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }
