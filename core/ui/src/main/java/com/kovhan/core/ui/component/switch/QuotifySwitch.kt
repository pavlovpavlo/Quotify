package com.kovhan.core.ui.component.switch

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.QuotifyMaterialTheme

private const val SWITCH_ANIM_MS = 180

@Composable
fun QuotifySwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val interactionSource = remember { MutableInteractionSource() }

    val trackColor by animateColorAsState(
        targetValue = if (checked) colors.accentSaved else colors.borderStrong,
        animationSpec = tween(SWITCH_ANIM_MS, easing = FastOutSlowInEasing),
        label = "switch-track",
    )
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 18.dp else 0.dp,
        animationSpec = tween(SWITCH_ANIM_MS, easing = FastOutSlowInEasing),
        label = "switch-thumb",
    )

    Box(
        modifier = modifier
            .size(width = 46.dp, height = 28.dp)
            .clip(CircleShape)
            .background(trackColor)
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange,
                role = Role.Switch,
                interactionSource = interactionSource,
                indication = null,
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .offset(x = thumbOffset)
                .size(22.dp)
                .clip(CircleShape)
                .background(Color.White),
        )
    }
}
