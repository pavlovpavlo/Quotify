package com.kovhan.feature.common.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.kovhan.core.navigation.ConfirmDialogKey
import com.kovhan.core.navigation.NavigationCoordinator
import kotlinx.coroutines.launch

@Composable
fun ConfirmDialogEntry(
    key: ConfirmDialogKey,
    coordinator: NavigationCoordinator,
) {
    val scope = rememberCoroutineScope()

    ConfirmDialog(
        iconRes = key.iconRes,
        title = stringResource(key.titleRes),
        message = stringResource(key.messageRes),
        confirmText = stringResource(key.confirmRes),
        cancelText = stringResource(key.cancelRes),
        onConfirm = {
            scope.launch {
                coordinator.dismissDialogWithResult(key.resultKey, key.payload ?: true)
            }
        },
        onDismiss = coordinator::dismissDialog,
    )
}
