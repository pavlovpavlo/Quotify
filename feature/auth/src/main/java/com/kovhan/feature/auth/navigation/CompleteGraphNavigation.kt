package com.kovhan.feature.auth.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.navigation
import com.kovhan.core.ui.navigation.CompleteGraph
import com.kovhan.core.ui.navigation.startDestination
import com.kovhan.feature.auth.presentation.complete.navigation.CompleteScreenNavAction
import com.kovhan.feature.auth.presentation.complete.navigation.completeScreen

fun NavController.navigateToCompleteGraph(builder: NavOptionsBuilder.() -> Unit = { }) {
    navigate(route = CompleteGraph, builder = builder)
}

fun NavGraphBuilder.completeGraph(
    navController: NavController,
    paddingValues: PaddingValues,
    navigateToSignUp: () -> Unit,
    navigateToSignIn: () -> Unit,
    navigateToMain: () -> Unit,
) {
    navigation<CompleteGraph>(startDestination = CompleteGraph.startDestination) {
        completeScreen(
            navAction = object : CompleteScreenNavAction {
                override fun navigateToSignUp() = navigateToSignUp()
                override fun navigateToSignIn() = navigateToSignIn()
                override fun navigateToMain() = navigateToMain()
            },
            paddingValues = paddingValues,
        )
    }
}
