package com.kovhan.feature.widget.presentation.appearance.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.kovhan.core.ui.component.switch.QuotifySwitch
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
internal fun WidgetAppearanceToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    expandedContent: @Composable (ColumnScope.() -> Unit)? = null,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val shape = RoundedCornerShape(dimensions.radiusLg)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.bgElevated)
            .border(dimensions.size1, colors.border, shape)
            .padding(horizontal = dimensions.size14, vertical = dimensions.size13),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensions.size12),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = typography.bodyStrong,
                    color = colors.textPrimary,
                )
                Text(
                    text = subtitle,
                    style = typography.caption,
                    color = colors.textSecondary,
                )
            }

            QuotifySwitch(checked = checked, onCheckedChange = onCheckedChange)
        }

        expandedContent?.invoke(this)
    }
}
