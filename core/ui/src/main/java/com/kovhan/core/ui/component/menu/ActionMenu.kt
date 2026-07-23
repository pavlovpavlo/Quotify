package com.kovhan.core.ui.component.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.kovhan.design.systems.QuotifyMaterialTheme

data class ActionMenuItem(
    val label: String,
    val iconRes: Int,
    val onClick: () -> Unit,
    val tint: Color? = null,
    val dividerBefore: Boolean = false,
)

/**
 * Content-width action popup: wraps its widest item, rounded, with an icon +
 * label per row and an optional divider before an item. Anchored to the
 * top-end of its parent, pushed down by [topOffset].
 */
@Composable
fun ActionMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    topOffset: Dp,
    items: List<ActionMenuItem>,
) {
    if (!expanded) return

    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val density = LocalDensity.current
    val menuShape = RoundedCornerShape(dimensions.radiusLg)

    Popup(
        alignment = Alignment.TopEnd,
        offset = with(density) { IntOffset(0, topOffset.roundToPx()) },
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true),
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .clip(menuShape)
                .background(colors.bgElevated)
                .border(dimensions.size1, colors.border, menuShape)
                .padding(dimensions.size6),
            verticalArrangement = Arrangement.spacedBy(dimensions.size1),
        ) {
            items.forEach { item ->
                if (item.dividerBefore) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimensions.size4, vertical = dimensions.size5)
                            .height(dimensions.size1)
                            .background(colors.borderSubtle),
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(dimensions.radiusMd))
                        .clickable {
                            onDismiss()
                            item.onClick()
                        }
                        .padding(horizontal = dimensions.size11, vertical = dimensions.size10),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.size11),
                ) {
                    Box(
                        modifier = Modifier.size(dimensions.size20),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            modifier = Modifier.size(dimensions.size18),
                            painter = painterResource(item.iconRes),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(item.tint ?: colors.textTertiary),
                        )
                    }
                    Text(
                        text = item.label,
                        style = typography.body.copy(fontWeight = FontWeight.W500),
                        color = item.tint ?: colors.textPrimary,
                    )
                }
            }
        }
    }
}
