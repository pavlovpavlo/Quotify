package com.kovhan.feature.webview.presentation.webview.navigation

import androidx.compose.runtime.Stable

@Stable
interface WebViewScreenNavAction {
    fun onBack()

    companion object {
        val Empty: WebViewScreenNavAction = object : WebViewScreenNavAction {
            override fun onBack() = Unit
        }
    }
}
