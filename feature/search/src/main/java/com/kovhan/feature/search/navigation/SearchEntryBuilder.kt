package com.kovhan.feature.search.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.kovhan.core.navigation.EntryBuilder
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.SearchKey
import com.kovhan.feature.search.presentation.search.navigation.SearchEntry
import javax.inject.Inject

class SearchEntryBuilder @Inject constructor() : EntryBuilder {

    override fun build(
        scope: EntryProviderScope<NavKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<SearchKey> {
            SearchEntry(
                coordinator = coordinator,
                paddingValues = paddingValues,
            )
        }
    }
}
