package com.kovhan.core.ui.component.playlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.stringResource
import com.kovhan.core.ui.component.switch.QuotifySwitch
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

/**
 * The two "add to playlist" toggles (home-screen widget + daily push) shown on
 * the add-quote details screen and the quote edit sheet. Tapping a row toggles it.
 */
@Composable
fun QuotifyPlaylistOptions(
    widgetEnabled: Boolean,
    pushEnabled: Boolean,
    onWidgetToggle: (Boolean) -> Unit,
    onPushToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val shape = RoundedCornerShape(dimensions.radiusXl)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.bgElevated)
            .border(dimensions.size1, colors.border, shape),
    ) {
        PlaylistOptionRow(
            title = stringResource(DsR.string.details_playlist_widget_title),
            subtitle = stringResource(DsR.string.details_playlist_widget_subtitle),
            iconRes = DsR.drawable.ic_widget,
            iconTint = colors.accentPremiumHover,
            iconContainer = colors.accentPremiumSoft,
            checked = widgetEnabled,
            onCheckedChange = onWidgetToggle,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.size1)
                .background(colors.border),
        )
        PlaylistOptionRow(
            title = stringResource(DsR.string.details_playlist_push_title),
            subtitle = stringResource(DsR.string.details_playlist_push_subtitle),
            iconRes = DsR.drawable.ic_bell,
            iconTint = colors.accentPrimary,
            iconContainer = colors.accentPrimarySoft,
            checked = pushEnabled,
            onCheckedChange = onPushToggle,
        )
    }
}

@Composable
private fun PlaylistOptionRow(
    title: String,
    subtitle: String,
    iconRes: Int,
    iconTint: Color,
    iconContainer: Color,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = dimensions.space4, vertical = dimensions.space3),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(dimensions.radiusLg))
                .background(iconContainer)
                .padding(dimensions.space3),
            contentAlignment = Alignment.Center,
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
                .padding(horizontal = dimensions.space3),
        ) {
            Text(
                text = title,
                style = typography.bodyStrong,
                color = colors.textPrimary,
            )
            Text(
                modifier = Modifier.padding(top = dimensions.size2),
                text = subtitle,
                style = typography.caption,
                color = colors.textTertiary,
            )
        }
        QuotifySwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}
