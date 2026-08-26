package com.kovhan.feature.main.presentation.faq.navigation

import androidx.compose.runtime.Stable

@Stable
interface FaqScreenNavAction {
    fun navigateBack()

    companion object {
        val Empty: FaqScreenNavAction = EmptyFaqScreenNavAction
    }
}

private object EmptyFaqScreenNavAction : FaqScreenNavAction {
    override fun navigateBack() = Unit
}
