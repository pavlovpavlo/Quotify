package com.kovhan.domain.ai


object AiLimits {
    const val FREE_DAILY = 6
    const val FREE_MONTHLY = 120
    const val SUBSCRIBED_DAILY = 16
    const val SUBSCRIBED_MONTHLY = 240

    fun dailyLimit(subscribed: Boolean): Int = if (subscribed) SUBSCRIBED_DAILY else FREE_DAILY

    fun monthlyLimit(subscribed: Boolean): Int = if (subscribed) SUBSCRIBED_MONTHLY else FREE_MONTHLY
}
