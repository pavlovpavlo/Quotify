package com.kovhan.feature.webview.presentation.webview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.kovhan.core.ui.component.QuotifyTopBar
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.webview.presentation.webview.navigation.WebViewScreenNavAction

@Composable
fun WebViewScreen(
    title: String,
    url: String,
    navAction: WebViewScreenNavAction,
    paddingValues: PaddingValues,
) {
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var loadProgress by remember { mutableIntStateOf(0) }

    val onBack: () -> Unit = {
        val webView = webViewRef
        if (webView != null && webView.canGoBack()) webView.goBack() else navAction.onBack()
    }

    BackHandler(enabled = true, onBack = onBack)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QuotifyMaterialTheme.colors.bgPrimary)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            ),
    ) {
        QuotifyTopBar(title = title, onBack = onBack)

        if (loadProgress in 1..99) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                progress = { loadProgress / 100f },
                color = QuotifyMaterialTheme.colors.accentPrimary,
                trackColor = QuotifyMaterialTheme.colors.bgSecondary,
            )
        }

        AndroidView(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            factory = { ctx ->
                buildConfiguredWebView(ctx = ctx, onProgress = { loadProgress = it })
                    .also { webViewRef = it }
            },
            update = { webView ->
                if (webView.url != url) webView.loadUrl(url)
            },
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun buildConfiguredWebView(
    ctx: Context,
    onProgress: (Int) -> Unit,
): WebView = WebView(ctx).apply {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT,
    )
    settings.javaScriptEnabled = true
    settings.domStorageEnabled = true
    settings.setSupportZoom(true)
    settings.builtInZoomControls = true
    settings.displayZoomControls = false

    webViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            onProgress(1)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            onProgress(100)
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?,
        ): Boolean = false
    }

    webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            onProgress(newProgress)
        }
    }
}
