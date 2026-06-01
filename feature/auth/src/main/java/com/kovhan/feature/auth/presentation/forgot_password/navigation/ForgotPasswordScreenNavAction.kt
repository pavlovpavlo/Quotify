package com.kovhan.feature.auth.presentation.forgot_password.navigation

import androidx.compose.runtime.Stable

@Stable
interface ForgotPasswordScreenNavAction {
    fun navigateBack()
    fun navigateToLogin()

    companion object {
        val Empty: ForgotPasswordScreenNavAction = EmptyForgotPasswordScreenNavAction
    }
}

private object EmptyForgotPasswordScreenNavAction : ForgotPasswordScreenNavAction {
    override fun navigateBack() = Unit
    override fun navigateToLogin() = Unit
} 