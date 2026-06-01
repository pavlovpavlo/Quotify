package com.kovhan.quotify.navigation.dock

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun DockFab(
    menuOpen: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val rotation by animateFloatAsState(
        targetValue = if (menuOpen) 45f else 0f,
        animationSpec = tween(durationMillis = FabRotateMs, easing = FabBounceEasing),
        label = "fabRotation",
    )
    val descriptionRes = if (menuOpen) R.string.dock_fab_close else R.string.dock_fab_open

    Box(
        modifier = modifier
            .shadow(
                elevation = 14.dp,
                shape = CircleShape,
                spotColor = colors.accentPrimary.copy(alpha = 0.55f),
                ambientColor = colors.accentPrimary.copy(alpha = 0.30f),
            )
            .size(FabSize)
            .clip(CircleShape)
            .background(colors.accentPrimary)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier
                .size(FabIconSize)
                .rotate(rotation),
            painter = painterResource(QuotifyMaterialTheme.images.dockFabOpen),
            contentDescription = stringResource(descriptionRes),
            colorFilter = ColorFilter.tint(colors.textOnAccent),
        )
    }
}
