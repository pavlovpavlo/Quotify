package com.kovhan.domain.ai

/**
 * Спільний пул AI-запитів: сканування та підказки тегів списують один лічильник.
 * Для безкоштовного плану він фактично покриває лише сканування — теги там
 * закриті цілком (див. [com.kovhan.domain.ai.use_case.CheckAiAccessUseCase]).
 */
object AiLimits {
    const val FREE_DAILY = 3
    const val FREE_MONTHLY = 50
    const val SUBSCRIBED_DAILY = 15
    const val SUBSCRIBED_MONTHLY = 200

    fun dailyLimit(subscribed: Boolean): Int = if (subscribed) SUBSCRIBED_DAILY else FREE_DAILY

    fun monthlyLimit(subscribed: Boolean): Int = if (subscribed) SUBSCRIBED_MONTHLY else FREE_MONTHLY
}
