package com.kovhan.feature.main.presentation.reminder_time.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.ReminderTimeSheetKey
import com.kovhan.feature.main.presentation.reminder_time.ReminderTimePickerSheet
import kotlinx.coroutines.launch

@Composable
internal fun ReminderTimeSheetEntry(
    key: ReminderTimeSheetKey,
    coordinator: NavigationCoordinator,
) {
    val scope = rememberCoroutineScope()

    ReminderTimePickerSheet(
        initialHour = key.hour,
        initialMinute = key.minute,
        onConfirm = { hour, minute ->
            scope.launch {
                coordinator.emitResult(NavigationCoordinator.KEY_REMINDER_TIME, hour to minute)
                coordinator.dismissBottomSheet()
            }
        },
        onDismiss = coordinator::dismissBottomSheet,
    )
}
