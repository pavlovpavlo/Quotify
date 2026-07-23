package com.kovhan.feature.main.presentation.theme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.ThemeSheetKey
import com.kovhan.feature.main.presentation.theme.ThemeBottomSheet
import kotlinx.coroutines.launch

@Composable
internal fun ThemeSheetEntry(
    key: ThemeSheetKey,
    coordinator: NavigationCoordinator,
) {
    val scope = rememberCoroutineScope()

    ThemeBottomSheet(
        selected = key.selected,
        onThemeSelected = { theme ->
            scope.launch {
                coordinator.emitResult(NavigationCoordinator.KEY_SELECTED_THEME, theme)
                coordinator.dismissBottomSheet()
            }
        },
        onDismiss = coordinator::dismissBottomSheet,
    )
}
