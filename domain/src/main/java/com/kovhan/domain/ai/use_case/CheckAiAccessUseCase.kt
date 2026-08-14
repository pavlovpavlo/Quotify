package com.kovhan.domain.ai.use_case

import com.kovhan.domain.ai.AiAccess
import com.kovhan.domain.ai.AiDenialReason
import com.kovhan.domain.ai.AiFeature
import com.kovhan.domain.ai.AiLimits
import com.kovhan.domain.ai.AiUsageRepository
import com.kovhan.domain.ai.SubscriptionRepository
import com.kovhan.domain.auth.use_case.IsLoggedInUseCase
import javax.inject.Inject

/**
 * Вирішує, чи може користувач запустити AI-функцію. Анонімним відмовляємо
 * одразу; підказки тегів — лише для передплатників; решта впирається у спільну
 * добову й місячну квоту, яку підписка піднімає.
 */
class CheckAiAccessUseCase @Inject constructor(
    private val isLoggedIn: IsLoggedInUseCase,
    private val subscriptionRepository: SubscriptionRepository,
    private val aiUsageRepository: AiUsageRepository,
) {
    suspend operator fun invoke(feature: AiFeature): AiAccess {
        if (!isLoggedIn()) return AiAccess.Denied(AiDenialReason.NOT_REGISTERED)

        val subscribed = subscriptionRepository.isSubscribed()
        if (feature == AiFeature.TAGS && !subscribed) {
            return AiAccess.Denied(AiDenialReason.SUBSCRIPTION_REQUIRED)
        }

        val usage = aiUsageRepository.getUsage()

        return when {
            usage.todayCount >= AiLimits.dailyLimit(subscribed) -> AiAccess.Denied(
                if (subscribed) {
                    AiDenialReason.DAILY_LIMIT_REACHED
                } else {
                    AiDenialReason.FREE_DAILY_LIMIT_REACHED
                },
            )

            usage.monthCount >= AiLimits.monthlyLimit(subscribed) -> AiAccess.Denied(
                if (subscribed) {
                    AiDenialReason.MONTHLY_LIMIT_REACHED
                } else {
                    AiDenialReason.FREE_MONTHLY_LIMIT_REACHED
                },
            )

            else -> AiAccess.Allowed
        }
    }
}
