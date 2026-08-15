package com.kovhan.core.ui.component.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kovhan.core.models.widget.WidgetBorderColor
import com.kovhan.core.models.widget.WidgetFontSize
import com.kovhan.core.models.widget.WidgetStyleSettings
import com.kovhan.core.models.widget.WidgetTextColor
import com.kovhan.core.models.widget.WidgetTones

/**
 * The widget as it is advertised across the app — a fixed classic look that does
 * not follow the user's own settings, so promo surfaces stay recognisable.
 * Shared so every place that shows off the widget shows the same thing.
 */
@Composable
fun WidgetShowcasePreview(
    quote: WidgetPreviewQuote,
    modifier: Modifier = Modifier,
    scale: Float = SHOWCASE_SCALE,
    cornerRadius: Dp = SHOWCASE_CORNER_RADIUS,
) {
    WidgetPreviewCard(
        modifier = modifier,
        settings = ShowcaseSettings,
        quote = quote,
        scale = scale,
        cornerRadius = cornerRadius,
    )
}

private val ShowcaseSettings = WidgetStyleSettings.Classic(
    toneId = WidgetTones.ALL[SHOWCASE_TONE_INDEX],
    borderEnabled = true,
    borderColor = WidgetBorderColor.Tone(WidgetTones.ALL[SHOWCASE_BORDER_TONE_INDEX]),
    fontSize = WidgetFontSize.EXTRA_LARGE,
    textColor = WidgetTextColor.Dark,
)

private const val SHOWCASE_TONE_INDEX = 8
private const val SHOWCASE_BORDER_TONE_INDEX = 1
private const val SHOWCASE_SCALE = WidgetPreviewDefaults.ThumbnailScale
private val SHOWCASE_CORNER_RADIUS = 14.dp
