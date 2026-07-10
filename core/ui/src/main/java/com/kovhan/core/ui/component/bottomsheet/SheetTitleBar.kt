package com.kovhan.core.ui.component.bottomsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.core.ui.component.button.QuotifyIconButton
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

/**
 * Centered sheet title with a trailing close (X) button — the header shared by
 * the "edit quote", "move to collection" and "save to collection" sheets.
 */
@Composable
fun SheetTitleBar(
    title: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    Box(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = title,
            style = QuotifyMaterialTheme.typography.h4,
            color = colors.textPrimary,
        )
        QuotifyIconButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = onClose,
            variant = QuotifyButtonVariant.Ghost,
            accent = QuotifyButtonAccent.Neutral,
            size = dimensions.iconXl,
            debounceInterval = 0L,
        ) {
            CompositionLocalProvider(LocalContentColor provides colors.textSecondary) {
                Image(
                    modifier = Modifier.size(dimensions.size18),
                    painter = painterResource(DsR.drawable.ic_close),
                    contentDescription = stringResource(DsR.string.add_quote_close_cd),
                    colorFilter = ColorFilter.tint(LocalContentColor.current),
                )
            }
        }
    }
}
