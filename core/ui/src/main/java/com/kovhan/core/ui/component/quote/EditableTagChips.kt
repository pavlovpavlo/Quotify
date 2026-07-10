package com.kovhan.core.ui.component.quote

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

/**
 * Editable `#tag` chip row: removable saved-tag pills followed by a dashed
 * "add tag" pill. Shared by the add-quote tag editor and the quote edit sheet.
 */
@Composable
fun EditableTagChips(
    tags: List<String>,
    onRemoveTag: (String) -> Unit,
    onAddTag: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = QuotifyMaterialTheme.dimensions

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensions.space2),
        verticalArrangement = Arrangement.spacedBy(dimensions.space2),
    ) {
        tags.forEach { tag ->
            TagChip(tag = tag, onRemove = { onRemoveTag(tag) })
        }
        TagAddPill(onClick = onAddTag)
    }
}

@Composable
private fun TagChip(
    tag: String,
    onRemove: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(dimensions.radiusFull))
            .background(colors.accentSavedSoft)
            .padding(start = dimensions.space3, end = dimensions.space1, top = dimensions.size5, bottom = dimensions.size5),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "#$tag",
            style = typography.caption.copy(fontWeight = FontWeight.W600),
            color = colors.accentSaved,
        )
        Box(
            modifier = Modifier
                .padding(start = dimensions.size2)
                .size(dimensions.size24)
                .clip(RoundedCornerShape(dimensions.radiusFull))
                .clickable(onClick = onRemove),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(dimensions.size14),
                painter = painterResource(DsR.drawable.ic_close),
                contentDescription = stringResource(DsR.string.details_tag_remove_cd),
                colorFilter = ColorFilter.tint(colors.accentSaved),
            )
        }
    }
}

@Composable
private fun TagAddPill(
    onClick: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    val strokeWidthPx = with(LocalDensity.current) { 1.dp.toPx() }
    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(strokeWidthPx * 5, strokeWidthPx * 4))
    val borderColor = colors.borderStrong
    val cornerPx = with(LocalDensity.current) { dimensions.radiusFull.toPx() }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(dimensions.radiusFull))
            .drawBehind {
                drawRoundRect(
                    color = borderColor,
                    cornerRadius = CornerRadius(cornerPx, cornerPx),
                    style = Stroke(width = strokeWidthPx, pathEffect = dashEffect),
                )
            }
            .clickable(onClick = onClick)
            .padding(start = dimensions.space3, end = dimensions.space4, top = dimensions.size5, bottom = dimensions.size5),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.size6),
    ) {
        Box(
            modifier = Modifier.size(dimensions.size24),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(dimensions.size14),
                painter = painterResource(DsR.drawable.ic_plus),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colors.textSecondary),
            )
        }
        Text(
            text = stringResource(DsR.string.details_tag_add),
            style = typography.caption.copy(fontWeight = FontWeight.W600),
            color = colors.textSecondary,
        )
    }
}
