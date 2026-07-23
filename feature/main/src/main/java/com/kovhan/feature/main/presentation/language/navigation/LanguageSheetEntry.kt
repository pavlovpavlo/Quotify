package com.kovhan.feature.main.presentation.language.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.kovhan.core.navigation.LanguageSheetKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.feature.main.presentation.language.LanguageBottomSheet
import kotlinx.coroutines.launch

@Composable
internal fun LanguageSheetEntry(
    key: LanguageSheetKey,
    coordinator: NavigationCoordinator,
) {
    val scope = rememberCoroutineScope()

    LanguageBottomSheet(
        selected = key.selected,
        onLanguageSelected = { language ->
            scope.launch {
                coordinator.emitResult(NavigationCoordinator.KEY_SELECTED_LANGUAGE, language)
                coordinator.dismissBottomSheet()
            }
        },
        onDismiss = coordinator::dismissBottomSheet,
    )
}
