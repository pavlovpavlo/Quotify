package com.kovhan.feature.subscription.presentation.paywall.mvi

interface PaywallIntent {

    fun onPlanSelected(basePlanId: String)

    fun onSubscribeClicked()

    fun onRestoreClicked()

    fun onRetryClicked()

    companion object {
        val Empty = object : PaywallIntent {
            override fun onPlanSelected(basePlanId: String) = Unit
            override fun onSubscribeClicked() = Unit
            override fun onRestoreClicked() = Unit
            override fun onRetryClicked() = Unit
        }
    }
}
