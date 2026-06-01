package com.kovhan.feature.auth.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.navigation
import com.kovhan.core.ui.navigation.AuthGraph
import com.kovhan.core.ui.navigation.startDestination
import com.kovhan.feature.auth.presentation.forgot_password.navigation.ForgotPasswordScreenNavAction
import com.kovhan.feature.auth.presentation.forgot_password.navigation.forgotPasswordScreen
import com.kovhan.feature.auth.presentation.login.navigation.LoginScreenNavAction
import com.kovhan.feature.auth.presentation.login.navigation.loginScreen
import com.kovhan.feature.auth.presentation.register.navigation.RegisterScreenNavAction
import com.kovhan.feature.auth.presentation.register.navigation.registerScreen

fun NavController.navigateToAuthGraph(builder: NavOptionsBuilder.() -> Unit = { }) {
    navigate(route = AuthGraph, builder = builder)
}

fun NavGraphBuilder.authGraph(
    navController: NavController,
    paddingValues: PaddingValues,
    navigateToMain: () -> Unit,
    navigateToWebView: (title: String, url: String) -> Unit,
) {
    navigation<AuthGraph>(startDestination = AuthGraph.startDestination) {

        loginScreen(
            navAction = object : LoginScreenNavAction {
                override fun navigateBack() {
                    navController.navigateUp()
                }
                override fun navigateToMain() {
                    navigateToMain()
                }
                override fun navigateToRegister() {
                    navController.navigate(AuthGraph.RegisterScreen) {
                        launchSingleTop = true
                    }
                }
                override fun navigateToForgotPassword() {
                    navController.navigate(AuthGraph.ForgotPasswordScreen) {
                        launchSingleTop = true
                    }
                }
                override fun navigateToWebView(title: String, url: String) {
                    navigateToWebView(title, url)
                }
            },
            paddingValues = paddingValues,
        )

        registerScreen(
            navAction = object : RegisterScreenNavAction {
                override fun navigateBack() {
                    navController.navigateUp()
                }
                override fun navigateToMain() {
                    navigateToMain()
                }
                override fun navigateToLogin() {
                    navController.navigate(AuthGraph.LoginScreen) {
                        popUpTo(AuthGraph.RegisterScreen) { inclusive = true }
                        launchSingleTop = true
                    }
                }
                override fun navigateToWebView(title: String, url: String) {
                    navigateToWebView(title, url)
                }
            },
            paddingValues = paddingValues,
        )

        forgotPasswordScreen(
            navAction = object : ForgotPasswordScreenNavAction {
                override fun navigateBack() {
                    navController.navigateUp()
                }
                override fun navigateToLogin() {
                    navController.navigate(AuthGraph.LoginScreen) {
                        popUpTo(AuthGraph.ForgotPasswordScreen) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            },
            paddingValues = paddingValues,
        )
    }
}
