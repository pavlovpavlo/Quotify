package com.kovhan.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

/** Contributes screen entries to the main [androidx.navigation3.ui.NavDisplay]. */
interface EntryBuilder {
    fun build(
        scope: EntryProviderScope<NavKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    )
}

/** Contributes bottom-sheet entries to the bottom-sheet NavDisplay. */
interface BottomSheetEntryBuilder {
    fun build(
        scope: EntryProviderScope<BottomSheetKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    )
}

/** Contributes dialog entries to the dialog NavDisplay. */
interface DialogEntryBuilder {
    fun build(
        scope: EntryProviderScope<DialogKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    )
}
