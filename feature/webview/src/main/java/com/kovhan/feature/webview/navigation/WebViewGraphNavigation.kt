package com.kovhan.feature.webview.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.navigation
import com.kovhan.core.ui.navigation.WebViewGraph
import com.kovhan.feature.webview.presentation.webview.navigation.WebViewScreenNavAction
import com.kovhan.feature.webview.presentation.webview.navigation.webViewScreen

/**
 * Open the WebView for an arbitrary external link. Call from anywhere:
 *
 *   navController.navigateToWebView(
 *       title = stringResource(R.string.privacy_policy),
 *       url = AppLinks.PRIVACY_POLICY,
 *   )
 */
fun NavController.navigateToWebView(
    title: String,
    url: String,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(
        route = WebViewGraph.WebViewScreen(title = title, url = url),
        builder = builder,
    )
}

fun NavGraphBuilder.webViewGraph(
    navController: NavController,
    paddingValues: PaddingValues,
) {
    navigation<WebViewGraph>(
        startDestination = WebViewGraph.WebViewScreen(title = "", url = ""),
    ) {
        webViewScreen(
            navAction = object : WebViewScreenNavAction {
                override fun onBack() {
                    navController.navigateUp()
                }
            },
            paddingValues = paddingValues,
        )
    }
}
