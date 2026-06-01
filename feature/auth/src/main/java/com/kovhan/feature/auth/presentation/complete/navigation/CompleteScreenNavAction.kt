package com.kovhan.feature.auth.presentation.complete.navigation

import androidx.compose.runtime.Stable

@Stable
interface CompleteScreenNavAction {
    fun navigateToSignUp()
    fun navigateToSignIn()
    fun navigateToMain()

    companion object {
        val Empty: CompleteScreenNavAction = object : CompleteScreenNavAction {
            override fun navigateToSignUp() = Unit
            override fun navigateToSignIn() = Unit
            override fun navigateToMain() = Unit
        }
    }
}
