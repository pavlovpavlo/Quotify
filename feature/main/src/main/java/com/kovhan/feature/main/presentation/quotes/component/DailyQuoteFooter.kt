package com.kovhan.feature.main.presentation.quotes.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
internal fun DailyQuoteFooter(
    book: String?,
    author: String?,
    isFavourite: Boolean,
    isFavouriteLoading: Boolean,
    onToggleFavourite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(dimensions.size2)) {
            book?.takeIf { it.isNotBlank() }?.let { source ->
                Text(
                    text = source,
                    color = colors.textPrimary,
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }
            author?.takeIf { it.isNotBlank() }?.let { name ->
                Text(
                    text = name,
                    color = colors.textTertiary,
                    style = TextStyle(
                        fontFamily = NewsreaderFamily,
                        fontStyle = FontStyle.Italic,
                        fontSize = 12.5.sp,
                    ),
                )
            }
        }

        DailyQuoteFavouriteButton(
            isFavourite = isFavourite,
            isLoading = isFavouriteLoading,
            onClick = onToggleFavourite,
        )
    }
}
