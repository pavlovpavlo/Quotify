package com.kovhan.quotify.navigation.dock

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun DockFab(
    menuOpen: Boolean,
    tipped: Boolean,
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
    val motionEnabled = rememberMotionEnabled()

    Box(
        modifier = modifier.size(FabSize),
        contentAlignment = Alignment.Center,
    ) {
        if (tipped && motionEnabled) {
            FabPulseRing()
        }

        Box(
            modifier = Modifier
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
}

@Composable
private fun FabPulseRing() {
    val colors = QuotifyMaterialTheme.colors

    val transition = rememberInfiniteTransition(label = "fabPulse")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = FabPulseMs, easing = EaseInOut),
            repeatMode = RepeatMode.Restart,
        ),
        label = "fabPulseProgress",
    )

    Box(
        modifier = Modifier
            .size(FabSize)
            .graphicsLayer {
                val current = 1f + progress * FabPulseMaxScale
                scaleX = current
                scaleY = current
                alpha = FabPulseMaxAlpha * (1f - progress)
            }
            .clip(CircleShape)
            .background(colors.accentPrimary),
    )
}
