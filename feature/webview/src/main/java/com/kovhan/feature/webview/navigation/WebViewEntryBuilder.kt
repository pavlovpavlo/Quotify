package com.kovhan.feature.webview.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.kovhan.core.navigation.EntryBuilder
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.WebViewKey
import com.kovhan.feature.webview.presentation.webview.navigation.WebViewEntry
import javax.inject.Inject

class WebViewEntryBuilder @Inject constructor() : EntryBuilder {

    override fun build(
        scope: EntryProviderScope<NavKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<WebViewKey> { key ->
            WebViewEntry(key = key, coordinator = coordinator, paddingValues = paddingValues)
        }
    }
}
