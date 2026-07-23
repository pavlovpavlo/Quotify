package com.kovhan.feature.addquote.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import com.kovhan.core.navigation.BottomSheetEntryBuilder
import com.kovhan.core.navigation.BottomSheetKey
import com.kovhan.core.navigation.NewCollectionSheetKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.SaveQuoteCollectionSheetKey
import com.kovhan.core.navigation.TagSheetKey
import com.kovhan.feature.addquote.presentation.details.component.tageditor.navigation.TagSheetEntry
import com.kovhan.feature.addquote.presentation.new_collection.navigation.NewCollectionSheetEntry
import com.kovhan.feature.addquote.presentation.save_collection.navigation.SaveQuoteCollectionSheetEntry
import javax.inject.Inject

class AddQuoteBottomSheetEntryBuilder @Inject constructor() : BottomSheetEntryBuilder {

    override fun build(
        scope: EntryProviderScope<BottomSheetKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<SaveQuoteCollectionSheetKey> { key ->
            SaveQuoteCollectionSheetEntry(
                key = key,
                coordinator = coordinator,
            )
        }

        scope.entry<NewCollectionSheetKey> { key ->
            NewCollectionSheetEntry(
                key = key,
                coordinator = coordinator,
            )
        }

        scope.entry<TagSheetKey> { key ->
            TagSheetEntry(
                key = key,
                coordinator = coordinator,
            )
        }
    }
}
