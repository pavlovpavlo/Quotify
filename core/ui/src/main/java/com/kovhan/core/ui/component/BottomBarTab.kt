package com.kovhan.core.ui.component

import androidx.compose.runtime.Composable

val BOTTOM_BAR_TABS = listOf<BottomBarTab>(
//    BottomBarTab.WALLET,
//    BottomBarTab.CRYPTO_CARDS,
//    BottomBarTab.MARKET,
//    BottomBarTab.SPOT,
//    BottomBarTab.FUTURES
)

sealed class BottomBarTab(
    val route: String,
    val iconSelected: @Composable () -> Int,
    val iconUnSelected: @Composable () -> Int
) {

//    data object MARKET : BottomBarTab(
//        route = EXCHANGE_DEX_GRAPH_START_ROUTE,
//        iconSelected = { HasherMaterialTheme.images.marketSelected },
//        iconUnSelected = { HasherMaterialTheme.images.marketUnselected },
//        isEnabled = false
//    )

}
