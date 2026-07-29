package com.kovhan.feature.widget.presentation.picker.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.core.ui.component.button.QuotifyIconButton
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

@Composable
internal fun PlaylistSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val shape = RoundedCornerShape(dimensions.radiusFull)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = dimensions.size16,
                end = dimensions.size16,
                top = dimensions.size4,
                bottom = dimensions.size10,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clip(shape)
                .background(colors.bgElevated)
                .border(dimensions.size1, colors.accentPrimary, shape)
                .padding(start = dimensions.size14, end = dimensions.size8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier.size(dimensions.size18),
                painter = painterResource(DsR.drawable.ic_search),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colors.accentPrimary),
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = dimensions.size10),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (query.isEmpty()) {
                    Text(
                        text = stringResource(DsR.string.playlist_search_placeholder),
                        style = typography.caption,
                        color = colors.textTertiary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                BasicTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = typography.caption.copy(color = colors.textPrimary),
                    cursorBrush = SolidColor(colors.accentPrimary),
                )
            }

            if (query.isNotEmpty()) {
                QuotifyIconButton(
                    onClick = onClear,
                    variant = QuotifyButtonVariant.Ghost,
                    accent = QuotifyButtonAccent.Neutral,
                    size = dimensions.size30,
                    debounceInterval = 0L,
                ) {
                    Image(
                        modifier = Modifier.size(dimensions.size18),
                        painter = painterResource(DsR.drawable.ic_close),
                        contentDescription = stringResource(DsR.string.search_clear_cd),
                        colorFilter = ColorFilter.tint(colors.textTertiary),
                    )
                }
            }
        }
    }
}
