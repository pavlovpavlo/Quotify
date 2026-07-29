package com.kovhan.feature.widget.presentation.picker.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

/** Round check marker used across the picker to show a source's picked state. */
@Composable
internal fun PickMarker(
    picked: Boolean,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors

    val background by animateColorAsState(
        targetValue = if (picked) colors.accentPrimary else colors.bgElevated,
        animationSpec = tween(160),
        label = "pick-bg",
    )
    val borderColor by animateColorAsState(
        targetValue = if (picked) colors.accentPrimary else colors.borderStrong,
        animationSpec = tween(160),
        label = "pick-border",
    )
    val checkScale by animateFloatAsState(
        targetValue = if (picked) 1f else 0f,
        animationSpec = tween(160),
        label = "pick-check",
    )

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(background)
            .border(1.5.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier
                .size(size * 0.54f)
                .scale(checkScale),
            painter = painterResource(DsR.drawable.ic_check),
            contentDescription = if (picked) stringResource(DsR.string.playlist_picked_cd) else null,
            colorFilter = ColorFilter.tint(colors.textOnAccent),
        )
    }
}
