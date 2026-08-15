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

        if (status.isEntitled()) {
            repository.rememberFreeSince(now)
            repository.clearShown(OfferTrigger.TENURE)
            repository.clearShown(OfferTrigger.CANCELED)
            return null
        }

        if (status.status == SubscriptionStatus.CANCELED) {
            return OfferTrigger.CANCELED.takeUnless { repository.isShown(it) }
        }

        val freeSince = repository.freeSinceAt().takeIf { it > 0L }
            ?: now.also { repository.rememberFreeSince(it) }

        val tenure = TimeUnit.DAYS.toMillis(PremiumLimits.OFFER_TENURE_DAYS)
        if (now - freeSince >= tenure && !repository.isShown(OfferTrigger.TENURE)) {
            return OfferTrigger.TENURE
        }
        return null
    }
}
