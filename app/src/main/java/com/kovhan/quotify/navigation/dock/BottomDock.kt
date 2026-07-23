package com.kovhan.quotify.navigation.dock

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kovhan.core.navigation.AddQuoteKey
import com.kovhan.core.navigation.AddQuoteTab
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.TabEnum
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.quotify.navigation.dock.mvi.DockIntent

@Composable
fun BottomDock(
    coordinator: NavigationCoordinator,
    isFabTooltipVisible: Boolean,
    uiIntent: DockIntent,
    modifier: Modifier = Modifier,
) {
    val currentTab = TabEnum.fromKey(coordinator.currentKey) ?: TabEnum.LIBRARY
    var menuOpen by remember { mutableStateOf(false) }

    val openAddQuote: (AddQuoteTab) -> Unit = { tab ->
        menuOpen = false
        coordinator.navigate(AddQuoteKey(tab))
    }

    val images = QuotifyMaterialTheme.images
    val actions = listOf(
        SpeedDialAction(R.string.dock_input_keyboard, images.dockInputKeyboard) {
            openAddQuote(AddQuoteTab.TEXT)
        },
        SpeedDialAction(R.string.dock_input_scan, images.dockInputScan) {
            openAddQuote(AddQuoteTab.SCAN)
        },
        SpeedDialAction(R.string.dock_input_voice, images.dockInputVoice) {
            openAddQuote(AddQuoteTab.VOICE)
        },
    )

    val tipped = isFabTooltipVisible && !menuOpen

    Box(modifier = modifier.fillMaxSize()) {
        DimScrim(visible = menuOpen, onDismiss = { menuOpen = false })

        SpeedDialMenu(
            visible = menuOpen,
            actions = actions,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(end = DockSideInset, bottom = SpeedDialBottomOffset + DockBottomInset),
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = DockSideInset, end = DockSideInset, bottom = DockBottomInset),
            horizontalArrangement = Arrangement.spacedBy(DockGap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GlassCapsule(
                modifier = Modifier.weight(1f),
                currentTab = currentTab,
                onTabSelected = { tab ->
                    if (menuOpen) menuOpen = false
                    if (tab != currentTab) coordinator.navigate(tab.key)
                },
            )
            DockFab(
                menuOpen = menuOpen,
                tipped = tipped,
                onClick = {
                    uiIntent.onFabClicked()
                    menuOpen = !menuOpen
                },
            )
        }

        AnimatedVisibility(
            visible = tipped,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(
                    end = DockSideInset,
                    bottom = DockBottomInset + FabSize + TooltipGap,
                ),
            enter = fadeIn(tween(TooltipFadeMs, easing = FolioEasing)),
            exit = fadeOut(tween(TooltipFadeMs)),
        ) {
            DockFabTooltip()
        }
    }
}
