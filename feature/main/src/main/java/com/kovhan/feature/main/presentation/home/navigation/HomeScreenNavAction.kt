package com.kovhan.feature.main.presentation.home.navigation

import androidx.compose.runtime.Stable

@Stable
interface HomeScreenNavAction {
    fun navigateBack()

    companion object {
        val Empty: HomeScreenNavAction = EmptyHomeScreenNavAction
    }
}

private object EmptyHomeScreenNavAction : HomeScreenNavAction {
    override fun navigateBack() = Unit
} 