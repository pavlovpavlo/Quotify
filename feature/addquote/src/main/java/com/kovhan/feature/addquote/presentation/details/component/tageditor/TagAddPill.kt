package com.kovhan.feature.addquote.presentation.details.component.tageditor

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun TagAddPill(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    val strokeWidthPx = with(androidx.compose.ui.platform.LocalDensity.current) { 1.dp.toPx() }
    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(strokeWidthPx * 5, strokeWidthPx * 4))
    val borderColor = colors.borderStrong
    val cornerPx = with(androidx.compose.ui.platform.LocalDensity.current) {
        dimensions.radiusFull.toPx()
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(dimensions.radiusFull))
            .drawBehind {
                drawRoundRect(
                    color = borderColor,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerPx, cornerPx),
                    style = Stroke(width = strokeWidthPx, pathEffect = dashEffect),
                )
            }
            .clickable(onClick = onClick)
            .padding(start = dimensions.space3, end = dimensions.space4, top = dimensions.size5, bottom = dimensions.size5),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(dimensions.size6),
    ) {
        Box(
            modifier = Modifier.size(dimensions.size24),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(dimensions.size14),
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colors.textSecondary),
            )
        }
        Text(
            text = stringResource(R.string.details_tag_add),
            style = typography.caption.copy(fontWeight = FontWeight.W600),
            color = colors.textSecondary,
        )
    }
}
