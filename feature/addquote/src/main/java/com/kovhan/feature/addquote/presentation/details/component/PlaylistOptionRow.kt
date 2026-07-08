package com.kovhan.feature.addquote.presentation.details.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.kovhan.core.ui.component.switch.QuotifySwitch
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
internal fun PlaylistOptionRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    iconRes: Int,
    iconTint: Color,
    iconContainer: Color,
    modifier: Modifier = Modifier,
) {
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.space4, vertical = dimensions.space3),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(dimensions.radiusLg))
                .background(iconContainer)
                .padding(dimensions.space3),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier.size(dimensions.iconMd),
                painter = painterResource(iconRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(iconTint),
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = dimensions.space3, end = dimensions.space3),
        ) {
            Text(
                text = title,
                style = typography.bodyStrong,
                color = QuotifyMaterialTheme.colors.textPrimary,
            )
            Text(
                modifier = Modifier.padding(top = dimensions.size2),
                text = subtitle,
                style = typography.caption,
                color = QuotifyMaterialTheme.colors.textTertiary,
            )
        }
        QuotifySwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}
