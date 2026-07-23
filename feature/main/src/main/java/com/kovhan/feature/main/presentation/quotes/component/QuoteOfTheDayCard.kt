package com.kovhan.feature.main.presentation.quotes.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.extensions.noRippleClickable
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

private val FolioEasing = CubicBezierEasing(0.22f, 0.61f, 0.36f, 1f)
private const val EXPAND_DURATION_MS = 220
private const val COLLAPSED_QUOTE_LINES = 2

@Composable
fun QuoteOfTheDayCard(
    text: String,
    author: String?,
    book: String?,
    isFavourite: Boolean,
    isFavouriteLoading: Boolean,
    onClose: () -> Unit,
    onToggleFavourite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val shape = RoundedCornerShape(dimensions.radiusXl)

    var expanded by remember { mutableStateOf(false) }
    // Enable the size animation only after the first layout, so the card doesn't
    // play an "expand" animation every time the Library screen (re)appears.
    var animateEnabled by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { animateEnabled = true }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(dimensions.size2, shape)
            .clip(shape)
            .background(colors.accentPremiumSoft)
            .border(dimensions.size1, colors.accentPremium.copy(alpha = 0.32f), shape)
            .noRippleClickable { expanded = !expanded }
            .then(
                if (animateEnabled) {
                    Modifier.animateContentSize(tween(EXPAND_DURATION_MS, easing = FolioEasing))
                } else {
                    Modifier
                },
            ),
    ) {
        DailyQuoteCloseButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(dimensions.size8_6),
            onClose = onClose)
        Column(
            modifier = Modifier
                .padding(
                    top = dimensions.size23,
                    bottom = dimensions.size16,
                    end = dimensions.size20,
                    start = dimensions.size20
                )
        ) {
            Text(
                text = stringResource(R.string.daily_quote_eyebrow),
                color = colors.accentPremiumHover,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.16.em,
                ),
            )

            Spacer(Modifier.height(dimensions.size9))

            Text(
                text = text,
                color = colors.textPrimary,
                maxLines = if (expanded) Int.MAX_VALUE else COLLAPSED_QUOTE_LINES,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontFamily = NewsreaderFamily,
                    fontStyle = FontStyle.Italic,
                    fontSize = 18.sp,
                    lineHeight = 25.6.sp,
                ),
            )

            Spacer(Modifier.height(dimensions.size11))

            DailyQuoteFooter(
                book = book,
                author = author,
                isFavourite = isFavourite,
                isFavouriteLoading = isFavouriteLoading,
                onToggleFavourite = onToggleFavourite,
            )
        }
    }
}