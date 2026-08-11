package com.kovhan.domain.billing.use_case

import com.kovhan.core.models.billing.SubscriptionStatus
import com.kovhan.domain.ai.SubscriptionRepository
import javax.inject.Inject

/** Повний стан для екрана керування підпискою: статус, дата завершення, авто-продовження. */
class GetSubscriptionStatusUseCase @Inject constructor(
    private val repository: SubscriptionRepository,
) {
    suspend operator fun invoke(): SubscriptionStatus = repository.getStatus()
}
