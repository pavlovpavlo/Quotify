package com.kovhan.feature.main.presentation.edit_field.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.kovhan.core.navigation.EditFieldSheetKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.feature.main.presentation.edit_field.EditFieldBottomSheet
import kotlinx.coroutines.launch

@Composable
internal fun EditFieldSheetEntry(
    key: EditFieldSheetKey,
    coordinator: NavigationCoordinator,
) {
    val scope = rememberCoroutineScope()

    EditFieldBottomSheet(
        field = key.field,
        initialValue = key.initialValue,
        onSave = { value ->
            scope.launch {
                coordinator.emitResult(NavigationCoordinator.KEY_EDIT_FIELD_VALUE, key.field to value)
                coordinator.dismissBottomSheet()
            }
        },
        onDismiss = coordinator::dismissBottomSheet,
    )
}
