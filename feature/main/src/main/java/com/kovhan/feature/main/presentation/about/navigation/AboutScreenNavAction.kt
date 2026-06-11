package com.kovhan.feature.main.presentation.about.navigation

import androidx.compose.runtime.Stable

@Stable
interface AboutScreenNavAction {
    fun navigateBack()

    companion object {
        val Empty: AboutScreenNavAction = EmptyAboutScreenNavAction
    }
}

private object EmptyAboutScreenNavAction : AboutScreenNavAction {
    override fun navigateBack() = Unit
}
