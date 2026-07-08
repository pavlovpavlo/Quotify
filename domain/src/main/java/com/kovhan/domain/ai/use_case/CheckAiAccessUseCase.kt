package com.kovhan.domain.ai.use_case

import com.kovhan.domain.ai.AiAccess
import com.kovhan.domain.ai.AiDenialReason
import com.kovhan.domain.ai.AiLimits
import com.kovhan.domain.ai.AiUsageRepository
import com.kovhan.domain.ai.SubscriptionRepository
import com.kovhan.domain.auth.use_case.IsLoggedInUseCase
import javax.inject.Inject

/**
 * Decides whether the current user may run an AI feature (OCR scan, tag
 * suggestion). Anonymous users are refused outright; registered users are
 * checked against their daily/monthly quota, which a subscription raises.
 */
class CheckAiAccessUseCase @Inject constructor(
    private val isLoggedIn: IsLoggedInUseCase,
    private val subscriptionRepository: SubscriptionRepository,
    private val aiUsageRepository: AiUsageRepository,
) {
    suspend operator fun invoke(): AiAccess {
        if (!isLoggedIn()) return AiAccess.Denied(AiDenialReason.NOT_REGISTERED)

        val subscribed = subscriptionRepository.isSubscribed()
        val usage = aiUsageRepository.getUsage()

        return when {
            usage.todayCount >= AiLimits.dailyLimit(subscribed) -> AiAccess.Denied(
                if (subscribed) AiDenialReason.DAILY_LIMIT_REACHED else AiDenialReason.FREE_LIMIT_REACHED,
            )
            usage.monthCount >= AiLimits.monthlyLimit(subscribed) -> AiAccess.Denied(
                if (subscribed) AiDenialReason.MONTHLY_LIMIT_REACHED else AiDenialReason.FREE_LIMIT_REACHED,
            )
            else -> AiAccess.Allowed
        }
    }
}
