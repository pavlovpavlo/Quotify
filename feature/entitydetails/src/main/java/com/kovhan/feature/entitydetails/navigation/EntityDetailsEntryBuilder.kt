package com.kovhan.feature.entitydetails.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.kovhan.core.navigation.EditQuoteKey
import com.kovhan.core.navigation.EntityDetailsKey
import com.kovhan.core.navigation.EntryBuilder
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.feature.entitydetails.presentation.entity_details.navigation.EntityDetailsEntry
import com.kovhan.feature.entitydetails.presentation.quote_edit.navigation.EntityQuoteEditEntry
import javax.inject.Inject

class EntityDetailsEntryBuilder @Inject constructor() : EntryBuilder {

    override fun build(
        scope: EntryProviderScope<NavKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<EntityDetailsKey> { key ->
            EntityDetailsEntry(
                key = key,
                coordinator = coordinator,
                paddingValues = paddingValues,
            )
        }

        scope.entry<EditQuoteKey> { key ->
            EntityQuoteEditEntry(
                key = key,
                coordinator = coordinator,
                paddingValues = paddingValues,
            )
        }
    }
}
