package com.kovhan.feature.auth.presentation.login.navigation

class LoginScreenNavAction(
    val navigateBack: () -> Unit = { },
    val navigateToRegister: () -> Unit = { },
    val navigateToForgotPassword: () -> Unit = { }
) 