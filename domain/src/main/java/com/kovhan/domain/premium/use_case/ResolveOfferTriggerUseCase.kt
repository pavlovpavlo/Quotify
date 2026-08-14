package com.kovhan.domain.premium.use_case

import com.kovhan.core.models.billing.SubscriptionStatus
import com.kovhan.core.models.billing.isEntitled
import com.kovhan.domain.ai.SubscriptionRepository
import com.kovhan.domain.premium.OfferPromptRepository
import com.kovhan.domain.premium.OfferTrigger
import com.kovhan.domain.premium.PremiumLimits
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ResolveOfferTriggerUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository,
    private val repository: OfferPromptRepository,
) {
    suspend operator fun invoke(now: Long = System.currentTimeMillis()): OfferTrigger? {
        val status = runCatching { subscriptionRepository.observeStatus().first() }
            .getOrNull() ?: return null

        if (status.isEntitled()) return null

        if (status.status == SubscriptionStatus.CANCELED) {
            return OfferTrigger.CANCELED.takeUnless { repository.isShown(it) }
        }

        val firstLaunchAt = repository.firstLaunchAt().takeIf { it > 0L }
            ?: now.also { repository.rememberFirstLaunch(it) }

        if (!repository.isShown(OfferTrigger.WELCOME)) return OfferTrigger.WELCOME

        val tenure = TimeUnit.DAYS.toMillis(PremiumLimits.OFFER_TENURE_DAYS)
        if (now - firstLaunchAt >= tenure && !repository.isShown(OfferTrigger.TENURE)) {
            return OfferTrigger.TENURE
        }
        return null
    }
}
