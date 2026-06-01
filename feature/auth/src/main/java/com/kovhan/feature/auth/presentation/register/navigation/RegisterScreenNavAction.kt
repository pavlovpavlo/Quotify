package com.kovhan.feature.auth.presentation.register.navigation

import androidx.compose.runtime.Stable

@Stable
interface RegisterScreenNavAction {
    fun navigateBack()
    fun navigateToMain()
    fun navigateToLogin()
    fun navigateToWebView(title: String, url: String)

    companion object {
        val Empty: RegisterScreenNavAction = object : RegisterScreenNavAction {
            override fun navigateBack() = Unit
            override fun navigateToMain() = Unit
            override fun navigateToLogin() = Unit
            override fun navigateToWebView(title: String, url: String) = Unit
        }
    }
}
