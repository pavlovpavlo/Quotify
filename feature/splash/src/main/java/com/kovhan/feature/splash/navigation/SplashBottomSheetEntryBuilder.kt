package com.kovhan.feature.splash.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.EntryProviderScope
import com.kovhan.core.navigation.BottomSheetEntryBuilder
import com.kovhan.core.navigation.BottomSheetKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.OfflineBlockingSheetKey
import com.kovhan.feature.splash.presentation.splash.component.OfflineBlockingSheet
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashBottomSheetEntryBuilder @Inject constructor() : BottomSheetEntryBuilder {

    override fun build(
        scope: EntryProviderScope<BottomSheetKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<OfflineBlockingSheetKey> {
            val coroutineScope = rememberCoroutineScope()
            OfflineBlockingSheet(
                onRetry = {
                    coroutineScope.launch {
                        coordinator.emitResult(NavigationCoordinator.KEY_OFFLINE_RETRY, true)
                        coordinator.dismissBottomSheet()
                    }
                },
                onDismiss = {
                    coroutineScope.launch {
                        coordinator.emitResult(NavigationCoordinator.KEY_OFFLINE_DISMISS, true)
                        coordinator.dismissBottomSheet()
                    }
                },
            )
        }
    }
}
