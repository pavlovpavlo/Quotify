package com.kovhan.feature.widget.presentation.frequency.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.WidgetFrequencySheetKey
import com.kovhan.feature.widget.presentation.frequency.WidgetFrequencySheet
import kotlinx.coroutines.launch

@Composable
internal fun WidgetFrequencySheetEntry(
    key: WidgetFrequencySheetKey,
    coordinator: NavigationCoordinator,
) {
    val scope = rememberCoroutineScope()

    WidgetFrequencySheet(
        initialHours = key.hours,
        onConfirm = { hours ->
            scope.launch {
                coordinator.emitResult(NavigationCoordinator.KEY_WIDGET_FREQUENCY, hours)
                coordinator.dismissBottomSheet()
            }
        },
        onDismiss = coordinator::dismissBottomSheet,
    )
}
