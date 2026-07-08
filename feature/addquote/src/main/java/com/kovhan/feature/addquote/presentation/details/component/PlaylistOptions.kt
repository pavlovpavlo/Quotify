package com.kovhan.feature.addquote.presentation.details.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun PlaylistOptions(
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
            .background(colors.bgElevated, shape)
            .border(dimensions.size1, colors.border, shape),
        verticalArrangement = Arrangement.spacedBy(dimensions.size1),
    ) {
        PlaylistOptionRow(
            title = stringResource(R.string.details_playlist_widget_title),
            subtitle = stringResource(R.string.details_playlist_widget_subtitle),
            checked = widgetEnabled,
            onCheckedChange = onWidgetToggle,
            iconRes = R.drawable.ic_widget,
            iconTint = colors.accentPremiumHover,
            iconContainer = colors.accentPremiumSoft,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.size1)
                .background(colors.border)
        )
        PlaylistOptionRow(
            title = stringResource(R.string.details_playlist_push_title),
            subtitle = stringResource(R.string.details_playlist_push_subtitle),
            checked = pushEnabled,
            onCheckedChange = onPushToggle,
            iconRes = R.drawable.ic_bell,
            iconTint = colors.accentPrimary,
            iconContainer = colors.accentPrimarySoft,
        )
    }
}
