package com.kovhan.feature.webview.presentation.webview.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.WebViewKey
import com.kovhan.feature.webview.presentation.webview.WebViewScreen

@Composable
internal fun WebViewEntry(
    key: WebViewKey,
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    WebViewScreen(
        title = key.title,
        url = key.url,
        navAction = object : WebViewScreenNavAction {
            override fun onBack() {
                coordinator.goBack()
            }
        },
        paddingValues = paddingValues,
    )
}
