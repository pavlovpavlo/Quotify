package com.kovhan.quotify.navigation.dock

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun DockFabTooltip(modifier: Modifier = Modifier) {
    val colors = QuotifyMaterialTheme.colors
    val shape = RoundedCornerShape(TooltipCorner)

    val motionEnabled = rememberMotionEnabled()
    val transition = rememberInfiniteTransition(label = "fabTooltip")
    val bobProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = TooltipBobMs, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "fabTooltipBob",
    )
    val bob = if (motionEnabled) TooltipBobOffset * -bobProgress else 0.dp

    Column(
        modifier = modifier.offset(y = bob),
        horizontalAlignment = Alignment.End,
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = TooltipMaxWidth)
                .shadow(
                    elevation = TooltipElevation,
                    shape = shape,
                    spotColor = TooltipShadow,
                    ambientColor = TooltipShadow,
                )
                .clip(shape)
                .background(colors.textPrimary)
                .padding(
                    horizontal = TooltipHorizontalPadding,
                    vertical = TooltipVerticalPadding,
                ),
        ) {
            Text(
                text = stringResource(R.string.dock_fab_tooltip),
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.W600,
                ),
                color = colors.bgElevated,
            )
        }

        Box(
            modifier = Modifier
                .padding(end = TooltipArrowEndInset)
                .offset(y = -TooltipArrowSize / 2)
                .size(TooltipArrowSize)
                .rotate(45f)
                .background(colors.textPrimary),
        )
    }
}
