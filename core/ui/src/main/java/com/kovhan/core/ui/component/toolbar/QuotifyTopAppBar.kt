package com.kovhan.core.ui.component.toolbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.max
import com.kovhan.design.systems.QuotifyMaterialTheme
import kotlin.math.max as maxInt

/**
 * Base top app bar with a centered [title]. The horizontal padding of the title
 * grows to match the wider of the [navigationIcon] / [actions] slots, so the title
 * stays optically centered regardless of how many actions are present.
 */
@Composable
fun QuotifyTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    containerColor: Color = QuotifyMaterialTheme.colors.bgPrimary,
    minSidePadding: Dp = QuotifyMaterialTheme.dimensions.iconXxl,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    var startWidthPx by remember { mutableIntStateOf(0) }
    var endWidthPx by remember { mutableIntStateOf(0) }

    val density = LocalDensity.current
    val sidePadding = with(density) {
        max(maxInt(startWidthPx, endWidthPx).toDp(), minSidePadding)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensions.topBarHeight),
        color = containerColor,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensions.space3),
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .onSizeChanged { startWidthPx = it.width },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                navigationIcon?.invoke()
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(horizontal = sidePadding),
                text = title,
                style = typography.bodyStrong,
                color = colors.textPrimary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .onSizeChanged { endWidthPx = it.width },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                actions()
            }
        }
    }
}

/**
 * Top app bar with a fully custom title slot, kept start-aligned next to an
 * optional [navigationIcon].
 */
@Composable
fun QuotifyTopAppBar(
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null,
    containerColor: Color = QuotifyMaterialTheme.colors.bgPrimary,
    customTitle: @Composable () -> Unit,
) {
    val dimensions = QuotifyMaterialTheme.dimensions

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensions.topBarHeight),
        color = containerColor,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensions.space3),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (navigationIcon != null) {
                navigationIcon()
                Spacer(modifier = Modifier.width(dimensions.space2))
            }
            customTitle()
        }
    }
}
