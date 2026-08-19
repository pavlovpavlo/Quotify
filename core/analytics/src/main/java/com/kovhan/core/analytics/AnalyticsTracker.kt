package com.kovhan.core.analytics

interface AnalyticsTracker {

    fun track(event: AnalyticsEvent)

    fun updateProfile(profile: AnalyticsProfile)

    fun setUserId(userId: String?)
}

data class AnalyticsProfile(
    val lifeDays: Int,
    val subscription: SubscriptionState,
    val theme: AppTheme,
    val language: String,
    val collectionCount: Int,
    val quoteCount: Int,
    val hasWidget: Boolean,
    val authType: AuthType,
)
