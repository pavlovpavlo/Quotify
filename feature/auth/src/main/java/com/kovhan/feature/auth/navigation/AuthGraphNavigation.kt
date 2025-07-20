package com.kovhan.feature.auth.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.navigation
import com.kovhan.core.ui.navigation.AuthGraph
import com.kovhan.core.ui.navigation.startDestination
import com.kovhan.feature.auth.presentation.login.navigation.LoginScreenNavAction
import com.kovhan.feature.auth.presentation.login.navigation.loginScreen
import com.kovhan.feature.auth.presentation.register.navigation.RegisterScreenNavAction
import com.kovhan.feature.auth.presentation.register.navigation.registerScreen
import com.kovhan.feature.auth.presentation.forgot_password.navigation.ForgotPasswordScreenNavAction
import com.kovhan.feature.auth.presentation.forgot_password.navigation.forgotPasswordScreen

fun NavController.navigateToAuthGraph(builder: NavOptionsBuilder.() -> Unit = { }){
    navigate(
        route = AuthGraph,
        builder = builder,
    )
}

fun NavGraphBuilder.authGraph(
    navController: NavController,
    paddingValues: PaddingValues
) {
    navigation<AuthGraph>(
        startDestination = AuthGraph.startDestination
    ) {
        loginScreen(
            navAction = LoginScreenNavAction(
                navigateBack = { navController.navigateUp() },
                navigateToRegister = {
                    navController.navigate(AuthGraph.RegisterScreen::class.qualifiedName!!)
                },
                navigateToForgotPassword = {
                    navController.navigate(AuthGraph.ForgotPasswordScreen::class.qualifiedName!!)
                }
            ),
            paddingValues = paddingValues
        )
        
        registerScreen(
            navAction = RegisterScreenNavAction(
                navigateBack = { navController.navigateUp() },
                navigateToLogin = {
                    navController.navigate(AuthGraph.LoginScreen::class.qualifiedName!!)
                }
            ),
            paddingValues = paddingValues
        )
        
        forgotPasswordScreen(
            navAction = ForgotPasswordScreenNavAction(
                navigateBack = { navController.navigateUp() },
                navigateToLogin = {
                    navController.navigate(AuthGraph.LoginScreen::class.qualifiedName!!)
                }
            ),
            paddingValues = paddingValues
        )
    }
} 