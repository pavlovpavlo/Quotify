package com.kovhan.feature.addquote.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.kovhan.core.navigation.AddQuoteKey
import com.kovhan.core.navigation.EntryBuilder
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.QuoteDetailsKey
import com.kovhan.feature.addquote.presentation.addquote.navigation.AddQuoteEntry
import com.kovhan.feature.addquote.presentation.details.navigation.DetailsEntry
import javax.inject.Inject

class AddQuoteEntryBuilder @Inject constructor() : EntryBuilder {

    override fun build(
        scope: EntryProviderScope<NavKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<AddQuoteKey> { key ->
            AddQuoteEntry(key = key, coordinator = coordinator, paddingValues = paddingValues)
        }
        scope.entry<QuoteDetailsKey> { key ->
            DetailsEntry(key = key, coordinator = coordinator, paddingValues = paddingValues)
        }
    }
}
