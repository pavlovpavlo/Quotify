package com.kovhan.feature.auth.presentation.login.navigation

import androidx.compose.runtime.Stable

@Stable
interface LoginScreenNavAction {
    fun navigateBack()
    fun navigateToMain()
    fun navigateToRegister()
    fun navigateToForgotPassword()
    fun navigateToWebView(title: String, url: String)

    companion object {
        val Empty: LoginScreenNavAction = object : LoginScreenNavAction {
            override fun navigateBack() = Unit
            override fun navigateToMain() = Unit
            override fun navigateToRegister() = Unit
            override fun navigateToForgotPassword() = Unit
            override fun navigateToWebView(title: String, url: String) = Unit
        }
    }
}
