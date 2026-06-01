package com.kovhan.feature.webview.presentation.webview.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kovhan.core.ui.navigation.WebViewGraph
import com.kovhan.feature.webview.presentation.webview.WebViewScreen

internal fun NavGraphBuilder.webViewScreen(
    navAction: WebViewScreenNavAction,
    paddingValues: PaddingValues,
) {
    composable<WebViewGraph.WebViewScreen> { backStackEntry ->
        val args: WebViewGraph.WebViewScreen = backStackEntry.toRoute()
        WebViewScreen(
            title = args.title,
            url = args.url,
            navAction = navAction,
            paddingValues = paddingValues,
        )
    }
}
