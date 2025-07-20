package com.kovhan.quotify.navigation

import androidx.compose.foundation.Image
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kovhan.core.ui.navigation.MainGraph
import com.kovhan.core.ui.navigation.TabEnum
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        TabEnum.HOME,
        TabEnum.QUOTES,
        TabEnum.FAVORITES,
        TabEnum.PROFILE
    )

    NavigationBar(
        containerColor = QuotifyMaterialTheme.colors.backgroundColor
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val currentTab = TabEnum.fromRoute(currentRoute)
        
        items.forEach { tab ->
            val isSelected = currentTab == tab
            
            NavigationBarItem(
                icon = {
                    Image(
                        painter = painterResource(id = when (tab) {
                            TabEnum.HOME -> QuotifyMaterialTheme.images.home
                            TabEnum.QUOTES -> QuotifyMaterialTheme.images.quotes
                            TabEnum.FAVORITES -> QuotifyMaterialTheme.images.favorites
                            TabEnum.PROFILE -> QuotifyMaterialTheme.images.profile
                        }),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(
                            color = if (isSelected) {
                                QuotifyMaterialTheme.colors.primary
                            } else {
                                QuotifyMaterialTheme.colors.secondary
                            }
                        )
                    )
                },
                label = { 
                    Text(
                        text = stringResource(id = when (tab) {
                            TabEnum.HOME -> R.string.home
                            TabEnum.QUOTES -> R.string.quotes
                            TabEnum.FAVORITES -> R.string.favorites
                            TabEnum.PROFILE -> R.string.profile
                        }),
                        color = if (isSelected) {
                            QuotifyMaterialTheme.colors.primary
                        } else {
                            QuotifyMaterialTheme.colors.secondary
                        }
                    )
                },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(tab.destination) {
                            popUpTo(MainGraph) {
                                saveState = true
                                inclusive = false
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
} 