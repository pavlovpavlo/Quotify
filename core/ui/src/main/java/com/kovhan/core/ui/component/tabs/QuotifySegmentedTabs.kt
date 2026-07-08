package com.kovhan.core.ui.component.tabs

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.kovhan.design.systems.QuotifyMaterialTheme

@Immutable
data class QuotifySegmentTab<T>(
    val value: T,
    val label: String,
    @DrawableRes val icon: Int? = null,
)

private val SelectorEasing = CubicBezierEasing(0.22f, 0.61f, 0.36f, 1f)

@Composable
fun <T> QuotifySegmentedTabs(
    items: List<QuotifySegmentTab<T>>,
    selected: T,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) return

    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    val selectedIndex = items.indexOfFirst { it.value == selected }.coerceAtLeast(0)
    val animatedIndex by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = tween(durationMillis = 260, easing = SelectorEasing),
        label = "SegmentedTabSelector",
    )

    val trackShape = RoundedCornerShape(dimensions.radiusFull)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .clip(trackShape)
            .background(colors.bgSecondary)
            .border(dimensions.size1, colors.border, trackShape)
            .padding(dimensions.space1),
    ) {
        val segmentWidth = maxWidth / items.size

        Box(
            modifier = Modifier
                .width(segmentWidth)
                .height(dimensions.size38)
                .offset(x = segmentWidth * animatedIndex)
                .clip(trackShape)
                .background(colors.bgElevated)
                .border(dimensions.size1, colors.borderStrong, trackShape),
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            items.forEach { item ->
                val isSelected = item.value == selected
                val contentColor = if (isSelected) colors.textPrimary else colors.textTertiary
                val interactionSource = remember { MutableInteractionSource() }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(dimensions.size38)
                        .clip(trackShape)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = { onSelect(item.value) },
                        ),
                    horizontalArrangement = Arrangement.spacedBy(
                        dimensions.size7,
                        Alignment.CenterHorizontally,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (item.icon != null) {
                        Image(
                            modifier = Modifier.size(dimensions.size17),
                            painter = painterResource(item.icon),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(contentColor),
                        )
                    }
                    Text(
                        text = item.label,
                        style = typography.caption.copy(
                            fontWeight = if (isSelected) FontWeight.W600 else FontWeight.W500,
                        ),
                        color = contentColor,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}
