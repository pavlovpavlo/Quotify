package com.kovhan.core.ui.component

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kovhan.core.ui.extensions.isNull
import com.kovhan.core.ui.extensions.orEmpty

@Composable
fun BottomBar(
    navController: NavHostController
) {
    val bottomBarRoutes = BOTTOM_BAR_TABS.map { it.route }

    val currentBackStack = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStack?.destination?.route
    val isVisibleBottomBar = currentBackStack?.isVisibleBottomBar(bottomBarRoutes).orEmpty()

    if (isVisibleBottomBar) {
        //var selectedItem: BottomBarTab by remember { mutableStateOf(BottomBarTab.WALLET) }
//        NavigationBar(
//            modifier = Modifier
//                .windowInsetsPadding(NavigationBarDefaults.windowInsets)
//                .height(HasherMaterialTheme.dimensions.bottomBarHeight),
//            containerColor = HasherMaterialTheme.colors.backgroundColor
//        ) {
//            BottomNavItem(
//                isSelected = currentRoute == BottomBarTab.WALLET.route || currentRoute == ROUTE_TOKEN_CARD,
//                bottomBarTab = BottomBarTab.WALLET,
//                title = stringResource(R.string.bottom_tab_wallet),
//                onClick = {
//                    onHomeTabClick()
//                    selectedItem = BottomBarTab.WALLET
//                    navController.navigateToBottomBarRoute(
//                        route = BottomBarTab.WALLET.route,
//                        currentRoute = navController.currentDestination?.route,
//                        rootRoute = HOME_GRAPH_START_ROUTE
//                    )
//                }
//            )
//
//        }
    }
}

private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.findStartDestination()) else graph
}

private fun NavBackStackEntry.isVisibleBottomBar(routeTabs: List<String>): Boolean {
    val currentRoute = destination.route
    val isBottomBarScreen = currentRoute in routeTabs
    return isBottomBarScreen || currentRoute.isNull()
}