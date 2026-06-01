package com.kovhan.quotify.navigation.dock

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import kotlinx.coroutines.delay

internal data class SpeedDialAction(
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int,
    val onClick: () -> Unit,
)

@Composable
internal fun BoxScope.DimScrim(visible: Boolean, onDismiss: () -> Unit) {
    AnimatedVisibility(
        visible = visible,
        modifier = Modifier.matchParentSize(),
        enter = fadeIn(animationSpec = tween(ScrimFadeMs)),
        exit = fadeOut(animationSpec = tween(ScrimFadeMs)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(QuotifyMaterialTheme.colors.bgOverlay.copy(alpha = 0.55f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss,
                ),
        )
    }
}

@Composable
internal fun SpeedDialMenu(
    visible: Boolean,
    actions: List<SpeedDialAction>,
    modifier: Modifier = Modifier,
) {
    val itemVisible = remember(actions.size) {
        mutableStateListOf<Boolean>().apply { repeat(actions.size) { add(false) } }
    }

    LaunchedEffect(visible) {
        if (visible) {
            for (i in actions.indices.reversed()) {
                itemVisible[i] = true
                delay(SpeedDialStaggerMs)
            }
        } else {
            for (i in actions.indices) itemVisible[i] = false
        }
    }

    if (!visible && itemVisible.none { it }) return

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(SpeedDialItemGap),
    ) {
        actions.forEachIndexed { index, action ->
            AnimatedVisibility(
                visible = itemVisible.getOrNull(index) == true,
                enter = fadeIn(tween(SpeedDialItemMs, easing = FolioEasing)) +
                    slideInVertically(tween(SpeedDialItemMs, easing = FolioEasing)) { it / 2 } +
                    scaleIn(tween(SpeedDialItemMs, easing = FolioEasing), initialScale = 0.96f),
                exit = fadeOut(tween(140)),
            ) {
                SpeedDialPill(action = action)
            }
        }
    }
}

@Composable
private fun SpeedDialPill(action: SpeedDialAction) {
    val colors = QuotifyMaterialTheme.colors
    Row(
        modifier = Modifier
            .shadow(8.dp, CircleShape, spotColor = WarmShadow, ambientColor = WarmShadow)
            .clip(CircleShape)
            .background(colors.bgElevated.copy(alpha = 0.92f))
            .border(1.dp, colors.borderStrong.copy(alpha = 0.55f), CircleShape)
            .clickable(onClick = action.onClick)
            .padding(horizontal = DockSideInset, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SpeedDialItemGap),
    ) {
        Text(
            text = stringResource(action.labelRes),
            style = TextStyle(
                fontFamily = InterFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.W600,
            ),
            color = colors.textPrimary,
        )
        Image(
            modifier = Modifier.size(SpeedDialIconSize),
            painter = painterResource(action.iconRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(colors.accentPrimary),
        )
    }
}
