package com.kovhan.quotify.navigation.dock

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.kovhan.core.navigation.TabEnum
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

private val DockTabs = listOf(TabEnum.LIBRARY, TabEnum.PROFILE)

@Composable
internal fun GlassCapsule(
    currentTab: TabEnum,
    onTabSelected: (TabEnum) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val capsuleShape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusFull)

    Box(
        modifier = modifier
            .shadow(10.dp, capsuleShape, spotColor = WarmShadow, ambientColor = WarmShadow)
            .height(CapsuleHeight)
            .clip(capsuleShape)
            .background(colors.bgElevated.copy(alpha = 0.78f))
            .border(1.dp, colors.borderStrong.copy(alpha = 0.55f), capsuleShape)
            .padding(CapsuleInnerPadding),
    ) {
        SelectionPill(currentTab = currentTab, tabCount = DockTabs.size)

        Row(modifier = Modifier.fillMaxSize()) {
            DockTabs.forEach { tab ->
                TabSlot(
                    tab = tab,
                    active = tab == currentTab,
                    onClick = { onTabSelected(tab) },
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                )
            }
        }
    }
}

@Composable
private fun BoxScope.SelectionPill(currentTab: TabEnum, tabCount: Int) {
    val colors = QuotifyMaterialTheme.colors
    val pillShape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusFull)
    val tabIndex = DockTabs.indexOf(currentTab).coerceAtLeast(0)

    val fraction by animateFloatAsState(
        targetValue = tabIndex.toFloat(),
        animationSpec = tween(durationMillis = PillTweenMs, easing = FolioEasing),
        label = "selectionPill",
    )

    BoxWithConstraints(modifier = Modifier.matchParentSize()) {
        val pillWidth = maxWidth / tabCount
        val pillWidthPx = with(androidx.compose.ui.platform.LocalDensity.current) {
            pillWidth.toPx()
        }
        Box(
            modifier = Modifier
                .width(pillWidth)
                .fillMaxHeight()
                .offset { IntOffset(x = (pillWidthPx * fraction).toInt(), y = 0) }
                .clip(pillShape)
                .background(colors.accentPrimarySoft)
                .border(1.dp, colors.accentPrimary.copy(alpha = 0.26f), pillShape),
        )
    }
}

@Composable
private fun TabSlot(
    tab: TabEnum,
    active: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val targetColor = if (active) colors.accentPrimary else colors.textTertiary
    val tint by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = TabColorTweenMs, easing = FolioEasing),
        label = "tabColor",
    )

    val iconRes = when (tab) {
        TabEnum.LIBRARY -> QuotifyMaterialTheme.images.dockTabLibrary
        TabEnum.PROFILE -> QuotifyMaterialTheme.images.dockTabProfile
    }
    val labelRes = when (tab) {
        TabEnum.LIBRARY -> R.string.dock_tab_library
        TabEnum.PROFILE -> R.string.dock_tab_profile
    }

    Column(
        modifier = modifier
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = Modifier.size(TabIconSize),
            painter = painterResource(iconRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(tint),
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = stringResource(labelRes),
            style = TextStyle(
                fontFamily = InterFamily,
                fontSize = 11.sp,
                fontWeight = FontWeight.W600,
                letterSpacing = 0.01.em,
            ),
            color = tint,
        )
    }
}
