package com.kovhan.feature.search.presentation.search.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.core.ui.component.button.QuotifyIconButton
import com.kovhan.core.ui.component.toolbar.QuotifyTopAppBar
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

@Composable
internal fun SearchTopBar(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    QuotifyTopAppBar(
        modifier = modifier,
        title = stringResource(DsR.string.search_title),
        navigationIcon = {
            QuotifyIconButton(
                onClick = onClose,
                variant = QuotifyButtonVariant.Ghost,
                accent = QuotifyButtonAccent.Neutral,
                size = dimensions.size38,
                debounceInterval = 0L,
            ) {
                CompositionLocalProvider(LocalContentColor provides colors.textSecondary) {
                    Image(
                        modifier = Modifier.size(dimensions.size22),
                        painter = painterResource(DsR.drawable.ic_back),
                        contentDescription = stringResource(DsR.string.details_back_cd),
                        colorFilter = ColorFilter.tint(LocalContentColor.current),
                    )
                }
            }
        },
    )
}
