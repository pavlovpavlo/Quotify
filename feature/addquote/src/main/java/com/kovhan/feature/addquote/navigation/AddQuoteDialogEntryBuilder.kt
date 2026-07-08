package com.kovhan.feature.addquote.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import com.kovhan.core.navigation.DialogEntryBuilder
import com.kovhan.core.navigation.DialogKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.TagSheetAiLimitDialogKey
import com.kovhan.feature.addquote.presentation.details.component.tageditor.navigation.TagSheetAiLimitDialogEntry
import javax.inject.Inject

class AddQuoteDialogEntryBuilder @Inject constructor() : DialogEntryBuilder {

    override fun build(
        scope: EntryProviderScope<DialogKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<TagSheetAiLimitDialogKey> { key ->
            TagSheetAiLimitDialogEntry(
                key = key,
                coordinator = coordinator,
            )
        }
    }
}
