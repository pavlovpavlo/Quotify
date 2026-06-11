package com.kovhan.feature.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.EntryProviderScope
import com.kovhan.core.navigation.DeleteAccountDialogKey
import com.kovhan.core.navigation.DialogEntryBuilder
import com.kovhan.core.navigation.DialogKey
import com.kovhan.core.navigation.LogoutDialogKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.design.systems.R
import com.kovhan.feature.main.presentation.edit_profile.component.ConfirmDialog
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainDialogEntryBuilder @Inject constructor() : DialogEntryBuilder {

    override fun build(
        scope: EntryProviderScope<DialogKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<LogoutDialogKey> {
            val coroutineScope = rememberCoroutineScope()
            ConfirmDialog(
                iconRes = R.drawable.ic_log_out,
                title = stringResource(R.string.dialog_logout_title),
                message = stringResource(R.string.dialog_logout_text),
                confirmText = stringResource(R.string.dialog_logout_confirm),
                cancelText = stringResource(R.string.dialog_cancel),
                onConfirm = {
                    coroutineScope.launch {
                        coordinator.emitResult(NavigationCoordinator.KEY_LOGOUT_CONFIRMED, true)
                        coordinator.dismissDialog()
                    }
                },
                onDismiss = coordinator::dismissDialog,
            )
        }

        scope.entry<DeleteAccountDialogKey> {
            val coroutineScope = rememberCoroutineScope()
            ConfirmDialog(
                iconRes = R.drawable.ic_trash,
                title = stringResource(R.string.dialog_delete_title),
                message = stringResource(R.string.dialog_delete_text),
                confirmText = stringResource(R.string.dialog_delete_confirm),
                cancelText = stringResource(R.string.dialog_cancel),
                onConfirm = {
                    coroutineScope.launch {
                        coordinator.emitResult(NavigationCoordinator.KEY_DELETE_CONFIRMED, true)
                        coordinator.dismissDialog()
                    }
                },
                onDismiss = coordinator::dismissDialog,
            )
        }
    }
}
