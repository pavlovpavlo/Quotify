package com.kovhan.feature.addquote.presentation.addquote.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kovhan.core.ui.component.button.QuotifyIconButton
import com.kovhan.core.ui.component.toolbar.QuotifyTopAppBar
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun AddQuoteTopBar(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    QuotifyTopAppBar(
        modifier = modifier,
        title = stringResource(R.string.add_quote_title),
        actions = {
            QuotifyIconButton(
                onClick = onClose,
                size = dimensions.iconXxl,
                debounceInterval = 0L,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        modifier = Modifier.size(dimensions.size22),
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = stringResource(R.string.add_quote_close_cd),
                        colorFilter = ColorFilter.tint(colors.textPrimary),
                    )
                }
            }
        },
    )
}
