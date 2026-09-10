package com.kovhan.core.ui.component.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.models.widget.WidgetStyleSettings
import com.kovhan.core.models.widget.WidgetTextAlign
import com.kovhan.core.ui.mapper.WidgetBackgroundMapper
import com.kovhan.core.ui.mapper.WidgetCoverMapper
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme

/** Quote shown inside a widget preview. */
data class WidgetPreviewQuote(
    val text: String,
    val author: String?,
    val book: String? = null,
)

object WidgetPreviewDefaults {
    /** Scale every small widget tile shares, so none looks bigger than the rest. */
    const val ThumbnailScale = 0.42f
}

/**
 * Renders one widget style exactly as the home screen will draw it. The same
 * composable backs the small style thumbnails and the large live preview in the
 * appearance editor — [scale] is the only difference, so the two can never
 * drift apart.
 */
@Composable
fun WidgetPreviewCard(
    settings: WidgetStyleSettings,
    quote: WidgetPreviewQuote,
    modifier: Modifier = Modifier,
    scale: Float = 1f,
    cornerRadius: Dp = PREVIEW_CORNER_RADIUS,
    metaMaxLines: Int = META_MAX_LINES,
) {
    val darkTheme = QuotifyMaterialTheme.system.isDarkTheme
    val shape = RoundedCornerShape(cornerRadius)
    val background = WidgetBackgroundMapper.fillColor(settings)
    val border = WidgetBackgroundMapper.borderColor(settings, darkTheme)
    val textColor = WidgetBackgroundMapper.textColor(settings, darkTheme)

    val horizontalAlignment = when (settings.textAlign) {
        WidgetTextAlign.START -> Alignment.Start
        WidgetTextAlign.CENTER -> Alignment.CenterHorizontally
    }
    val textAlign = when (settings.textAlign) {
        WidgetTextAlign.START -> TextAlign.Start
        WidgetTextAlign.CENTER -> TextAlign.Center
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(background)
            .then(
                if (border != null) {
                    Modifier.border(BORDER_WIDTH * scale, border, shape)
                } else {
                    Modifier
                },
            ),
    ) {
        if (settings is WidgetStyleSettings.Cover) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(
                    WidgetCoverMapper.imageRes(settings.coverId, settings.blurEnabled),
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(CONTENT_PADDING * scale),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = horizontalAlignment,
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                text = quote.text,
                color = textColor,
                textAlign = textAlign,
                maxLines = QUOTE_MAX_LINES,
                overflow = TextOverflow.Ellipsis,
                fontFamily = NewsreaderFamily,
                fontStyle = FontStyle.Italic,
                fontSize = QUOTE_FONT_SIZE * settings.fontSize.scale * scale,
                lineHeight = QUOTE_LINE_HEIGHT * settings.fontSize.scale * scale,
            )

            val meta = listOfNotNull(
                quote.author?.takeIf { it.isNotBlank() },
                quote.book?.takeIf { it.isNotBlank() },
            ).joinToString(" — ")

            if (meta.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = META_SPACING * scale),
                    text = meta,
                    color = textColor.copy(alpha = AUTHOR_ALPHA),
                    textAlign = textAlign,
                    maxLines = metaMaxLines,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.W600,
                    fontSize = META_FONT_SIZE * scale,
                )
            }
        }
    }
}

private val PREVIEW_CORNER_RADIUS = 20.dp
private val BORDER_WIDTH = 1.5.dp
private val CONTENT_PADDING = 18.dp
private val META_SPACING = 10.dp
private val QUOTE_FONT_SIZE = 17.sp
private val QUOTE_LINE_HEIGHT = 23.sp
private val META_FONT_SIZE = 12.sp
private const val AUTHOR_ALPHA = 0.82f
private const val QUOTE_MAX_LINES = 6
private const val META_MAX_LINES = 1
