package com.kovhan.core.ui.component.bottomsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.core.ui.component.button.QuotifyIconButton
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

/**
 * Standard bottom sheet header: a centered [title] flanked by navigation
 * buttons — a trailing close (X) via [onClose] and an optional leading back
 * chevron via [onBack]. There is no drag handle by design.
 *
 * Renders nothing when [title] is null, so sheets that don't need a header
 * (e.g. a blocking offline gate) stay chromeless.
 */
@Composable
internal fun BottomSheetHeader(
    title: String?,
    modifier: Modifier = Modifier,
    onClose: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null,
) {
    if (title == null) return

    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = dimensions.space5,
                bottom = dimensions.space3,
                start = dimensions.space5,
                end = dimensions.space5,
            ),
    ) {
        if (onBack != null) {
            SheetHeaderIconButton(
                modifier = Modifier.align(Alignment.CenterStart),
                icon = DsR.drawable.ic_back,
                contentDescription = stringResource(DsR.string.details_back_cd),
                onClick = onBack,
            )
        }

        Text(
            text = title,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = dimensions.size44),
            style = QuotifyMaterialTheme.typography.h4,
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        if (onClose != null) {
            SheetHeaderIconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                icon = DsR.drawable.ic_close,
                contentDescription = stringResource(DsR.string.add_quote_close_cd),
                onClick = onClose,
            )
        }
    }
}

@Composable
private fun SheetHeaderIconButton(
    icon: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    QuotifyIconButton(
        modifier = modifier,
        onClick = onClick,
        variant = QuotifyButtonVariant.Ghost,
        accent = QuotifyButtonAccent.Neutral,
        size = dimensions.size44,
        debounceInterval = 0L,
    ) {
        CompositionLocalProvider(LocalContentColor provides colors.textSecondary) {
            Image(
                modifier = Modifier.size(dimensions.iconLg),
                painter = painterResource(icon),
                contentDescription = contentDescription,
                colorFilter = ColorFilter.tint(LocalContentColor.current),
            )
        }
    }
}
