package com.kovhan.core.ui.component.quote

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import com.kovhan.core.models.EnrichedQuote
import com.kovhan.core.ui.mapper.collectionColor
import com.kovhan.design.systems.QuotifyMaterialTheme

/**
 * Non-interactive quote card used by Search results and read-only lists.
 * Mirrors the interactive quote card (left tone rail, quote text, meta, tags)
 * but has no menu.
 * Occurrences of [highlightQuery] in the quote text are highlighted.
 */
@Composable
fun ReadOnlyQuoteCard(
    quote: EnrichedQuote,
    highlightQuery: String? = null,
    modifier: Modifier = Modifier,
    railColor: Color = collectionColor(quote.collection?.iconColor ?: "terra"),
    trailingAction: (@Composable BoxScope.() -> Unit)? = null,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val shape = RoundedCornerShape(dimensions.radiusLg)

    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(colors.bgElevated)
                .border(dimensions.size1, colors.border, shape)
                .padding(
                    start = dimensions.size20,
                    top = dimensions.size16,
                    end = dimensions.size16,
                    bottom = dimensions.size14,
                ),
        ) {
            if (trailingAction != null) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.padding(end = dimensions.size28),
                        text = highlighted(quote.text, highlightQuery, colors.accentPremiumSoft),
                        style = typography.readingBody.copy(
                            color = colors.textPrimary,
                            fontStyle = FontStyle.Italic,
                        ),
                    )
                    trailingAction()
                }
            } else {
                Text(
                    text = highlighted(quote.text, highlightQuery, colors.accentPremiumSoft),
                    style = typography.readingBody.copy(
                        color = colors.textPrimary,
                        fontStyle = FontStyle.Italic,
                    ),
                )
            }

            val author = quote.author?.name
            val book = quote.book?.name
            if (!author.isNullOrBlank() || !book.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(dimensions.size12))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(dimensions.size8),
                    verticalArrangement = Arrangement.spacedBy(dimensions.size4),
                ) {
                    if (!author.isNullOrBlank()) {
                        Text(
                            text = author,
                            style = typography.bodyStrong,
                            color = colors.textSecondary,
                        )
                    }
                    if (!book.isNullOrBlank()) {
                        Text(
                            text = "— $book",
                            style = typography.readingBody.copy(fontStyle = FontStyle.Italic),
                            color = colors.textTertiary,
                        )
                    }
                }
            }

            if (quote.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(dimensions.size12))
                QuoteTagChips(tags = quote.tags.map { it.name })
            }
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(vertical = dimensions.size14),
            contentAlignment = Alignment.CenterStart,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(dimensions.size3)
                    .clip(RoundedCornerShape(dimensions.radiusFull))
                    .background(railColor),
            )
        }
    }
}

private fun highlighted(text: String, query: String?, highlightBg: Color): AnnotatedString {
    if (query.isNullOrBlank()) return AnnotatedString(text)
    val lower = text.lowercase()
    val needle = query.trim().lowercase()
    if (needle.isEmpty() || !lower.contains(needle)) return AnnotatedString(text)

    return buildAnnotatedString {
        var start = 0
        var idx = lower.indexOf(needle, start)
        while (idx >= 0) {
            append(text.substring(start, idx))
            withStyle(SpanStyle(background = highlightBg)) {
                append(text.substring(idx, idx + needle.length))
            }
            start = idx + needle.length
            idx = lower.indexOf(needle, start)
        }
        append(text.substring(start))
    }
}
