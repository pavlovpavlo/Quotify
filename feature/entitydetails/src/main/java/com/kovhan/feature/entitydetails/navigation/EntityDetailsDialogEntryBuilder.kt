package com.kovhan.feature.entitydetails.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import com.kovhan.core.navigation.ConfirmDialogKey
import com.kovhan.core.navigation.DialogEntryBuilder
import com.kovhan.core.navigation.DialogKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.feature.common.component.dialog.ConfirmDialogEntry
import javax.inject.Inject

class EntityDetailsDialogEntryBuilder @Inject constructor() : DialogEntryBuilder {

    override fun build(
        scope: EntryProviderScope<DialogKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<ConfirmDialogKey> { key ->
            ConfirmDialogEntry(
                key = key,
                coordinator = coordinator,
            )
        }
    }
}
