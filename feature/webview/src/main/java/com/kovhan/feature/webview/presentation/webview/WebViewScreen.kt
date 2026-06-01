package com.kovhan.feature.webview.presentation.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.kovhan.core.ui.component.button.QuotifyIconButton
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.webview.presentation.webview.navigation.WebViewScreenNavAction

@Composable
fun WebViewScreen(
    title: String,
    url: String,
    navAction: WebViewScreenNavAction,
    paddingValues: PaddingValues,
) {
    val context = LocalContext.current
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var loadProgress by remember { mutableIntStateOf(0) }

    // Hardware back button: let the WebView pop its own history before we leave the screen.
    BackHandler(enabled = true) {
        val wv = webViewRef
        if (wv != null && wv.canGoBack()) wv.goBack() else navAction.onBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QuotifyMaterialTheme.colors.bgPrimary)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            ),
    ) {
        WebViewTopBar(
            title = title,
            onBack = {
                val wv = webViewRef
                if (wv != null && wv.canGoBack()) wv.goBack() else navAction.onBack()
            },
        )

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
                buildConfiguredWebView(
                    ctx = ctx,
                    onProgress = { loadProgress = it },
                ).also { webViewRef = it }
            },
            update = { wv ->
                if (wv.url != url) wv.loadUrl(url)
            },
        )
    }
}

@Composable
private fun WebViewTopBar(
    title: String,
    onBack: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(QuotifyMaterialTheme.dimensions.topBarHeight)
            .padding(horizontal = QuotifyMaterialTheme.dimensions.space2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        QuotifyIconButton(
            onClick = onBack,
            size = 40.dp,
        ) {
            // Reuse splash chevron / generic back — use an arrow-shaped vector if you
            // have one in design-systems. For now we render a simple rotated chevron
            // using the text color via LocalContentColor.
            Text(
                text = "‹",
                color = QuotifyMaterialTheme.colors.textPrimary,
                style = QuotifyMaterialTheme.typography.h3,
                modifier = Modifier.graphicsLayer { translationY = -2f },
            )
        }

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = QuotifyMaterialTheme.dimensions.space2),
            text = title,
            style = QuotifyMaterialTheme.typography.h4,
            color = QuotifyMaterialTheme.colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        // Reserve symmetric space so the title stays optically centered.
        Box(modifier = Modifier.size(40.dp))
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun buildConfiguredWebView(
    ctx: android.content.Context,
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
        ): Boolean = false // keep navigation inside our WebView
    }

    webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            onProgress(newProgress)
        }
    }
}
