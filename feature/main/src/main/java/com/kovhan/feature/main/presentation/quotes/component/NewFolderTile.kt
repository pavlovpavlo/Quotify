package com.kovhan.feature.main.presentation.quotes.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun NewFolderTile(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val shape = RoundedCornerShape(dimensions.radiusLg)

    val strokeWidthPx = with(LocalDensity.current) { dimensions.size1.toPx() }
    val cornerPx = with(LocalDensity.current) { dimensions.radiusLg.toPx() }
    val dash = PathEffect.dashPathEffect(floatArrayOf(strokeWidthPx * 5, strokeWidthPx * 4))
    val borderColor = colors.borderStrong

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = dimensions.size7)
            .clip(shape)
            .drawBehind {
                drawRoundRect(
                    color = borderColor,
                    cornerRadius = CornerRadius(cornerPx, cornerPx),
                    style = Stroke(width = strokeWidthPx, pathEffect = dash),
                )
            }
            .clickable(onClick = onClick)
            .padding(dimensions.size15),
        verticalArrangement = Arrangement.spacedBy(dimensions.space2, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(dimensions.size30)
                .clip(RoundedCornerShape(dimensions.radiusFull))
                .background(colors.bgSecondary),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(dimensions.size16),
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colors.accentPrimary),
            )
        }
        Text(
            text = stringResource(R.string.library_new_folder),
            style = typography.caption,
            color = colors.textSecondary,
        )
    }
}
