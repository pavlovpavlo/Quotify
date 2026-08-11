package com.kovhan.domain.billing.use_case

import com.kovhan.domain.ai.SubscriptionRepository
import javax.inject.Inject

class IsSubscribedUseCase @Inject constructor(
    private val repository: SubscriptionRepository,
) {
    suspend operator fun invoke(): Boolean = repository.isSubscribed()
}
