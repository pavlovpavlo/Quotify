package com.kovhan.feature.splash.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.EntryProviderScope
import com.kovhan.core.navigation.DialogEntryBuilder
import com.kovhan.core.navigation.DialogKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.UpdateRequiredDialogKey
import com.kovhan.feature.splash.presentation.splash.component.UpdateRequiredDialog
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashDialogEntryBuilder @Inject constructor() : DialogEntryBuilder {

    override fun build(
        scope: EntryProviderScope<DialogKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<UpdateRequiredDialogKey> {
            val coroutineScope = rememberCoroutineScope()
            UpdateRequiredDialog(
                onUpdate = {
                    coroutineScope.launch {
                        coordinator.emitResult(NavigationCoordinator.KEY_UPDATE_REQUIRED_UPDATE, true)
                    }
                },
                onExit = {
                    coroutineScope.launch {
                        coordinator.emitResult(NavigationCoordinator.KEY_UPDATE_REQUIRED_EXIT, true)
                    }
                },
            )
        }
    }
}
