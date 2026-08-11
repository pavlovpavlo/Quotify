package com.kovhan.domain.billing.use_case

import com.kovhan.core.models.billing.SubscriptionStatus
import com.kovhan.domain.ai.SubscriptionRepository
import javax.inject.Inject

/** Примусово перечитує статус із Firestore — після верифікації покупки чи pull-to-refresh. */
class RefreshSubscriptionUseCase @Inject constructor(
    private val repository: SubscriptionRepository,
) {
    suspend operator fun invoke(): SubscriptionStatus = repository.refresh()
}
