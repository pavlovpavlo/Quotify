package com.kovhan.feature.widget.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.EntryProviderScope
import com.kovhan.core.navigation.DialogEntryBuilder
import com.kovhan.core.navigation.DialogKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.WidgetExitAction
import com.kovhan.core.navigation.WidgetPromoKey
import com.kovhan.core.navigation.WidgetSettingsExitDialogKey
import com.kovhan.feature.widget.presentation.promo.WidgetPromoDialogEntry
import com.kovhan.feature.widget.presentation.settings.component.WidgetSettingsExitDialog
import kotlinx.coroutines.launch
import javax.inject.Inject

class WidgetDialogEntryBuilder @Inject constructor() : DialogEntryBuilder {

    override fun build(
        scope: EntryProviderScope<DialogKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<WidgetPromoKey> { key ->
            WidgetPromoDialogEntry(
                key = key,
                coordinator = coordinator,
            )
        }

        scope.entry<WidgetSettingsExitDialogKey> {
            val coroutineScope = rememberCoroutineScope()

            fun finish(action: WidgetExitAction) {
                coroutineScope.launch {
                    coordinator.dismissDialogWithResult(
                        NavigationCoordinator.KEY_WIDGET_SETTINGS_EXIT,
                        action,
                    )
                }
            }

            WidgetSettingsExitDialog(
                onApply = { finish(WidgetExitAction.APPLY) },
                onLeave = { finish(WidgetExitAction.LEAVE) },
                onStay = coordinator::dismissDialog,
            )
        }
    }
}
