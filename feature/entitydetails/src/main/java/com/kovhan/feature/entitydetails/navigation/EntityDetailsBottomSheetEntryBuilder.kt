package com.kovhan.feature.entitydetails.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import com.kovhan.core.navigation.BottomSheetEntryBuilder
import com.kovhan.core.navigation.BottomSheetKey
import com.kovhan.core.navigation.EditCollectionStyleSheetKey
import com.kovhan.core.navigation.EditQuoteSheetKey
import com.kovhan.core.navigation.MoveQuoteSheetKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.RenameEntitySheetKey
import com.kovhan.feature.entitydetails.presentation.collection_style.navigation.EditCollectionStyleSheetEntry
import com.kovhan.feature.entitydetails.presentation.move_quote.navigation.EntityMoveQuoteSheetEntry
import com.kovhan.feature.entitydetails.presentation.quote_edit.navigation.EntityQuoteEditSheetEntry
import com.kovhan.feature.entitydetails.presentation.rename_entity.navigation.RenameEntitySheetEntry
import javax.inject.Inject

class EntityDetailsBottomSheetEntryBuilder @Inject constructor() : BottomSheetEntryBuilder {

    override fun build(
        scope: EntryProviderScope<BottomSheetKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<RenameEntitySheetKey> { key ->
            RenameEntitySheetEntry(
                key = key,
                coordinator = coordinator,
            )
        }

        scope.entry<EditCollectionStyleSheetKey> { key ->
            EditCollectionStyleSheetEntry(
                key = key,
                coordinator = coordinator,
            )
        }

        scope.entry<EditQuoteSheetKey> { key ->
            EntityQuoteEditSheetEntry(
                key = key,
                coordinator = coordinator,
            )
        }

        scope.entry<MoveQuoteSheetKey> { key ->
            EntityMoveQuoteSheetEntry(
                key = key,
                coordinator = coordinator,
            )
        }
    }
}
