package com.kovhan.feature.splash.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.EntryProviderScope
import com.kovhan.core.navigation.DialogEntryBuilder
import com.kovhan.core.navigation.DialogKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.OfflineBlockingDialogKey
import com.kovhan.feature.splash.presentation.splash.component.OfflineBlockingDialog
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashDialogEntryBuilder @Inject constructor() : DialogEntryBuilder {

    override fun build(
        scope: EntryProviderScope<DialogKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<OfflineBlockingDialogKey> {
            val coroutineScope = rememberCoroutineScope()
            OfflineBlockingDialog(
                onRetry = {
                    coroutineScope.launch {
                        coordinator.emitResult(NavigationCoordinator.KEY_OFFLINE_RETRY, true)
                        coordinator.dismissDialog()
                    }
                },
                onDismiss = {
                    coroutineScope.launch {
                        coordinator.emitResult(NavigationCoordinator.KEY_OFFLINE_DISMISS, true)
                        coordinator.dismissDialog()
                    }
                },
            )
        }
    }
}
