package com.kovhan.domain.billing.use_case

import com.kovhan.core.models.billing.SubscriptionStatus
import com.kovhan.domain.ai.SubscriptionRepository
import com.kovhan.domain.billing.reemitOnExpiry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Потік стану підписки з локального кешу — оновлюється сам після кожної верифікації. */
class ObserveSubscriptionStatusUseCase @Inject constructor(
    private val repository: SubscriptionRepository,
) {
    operator fun invoke(): Flow<SubscriptionStatus> =
        repository.observeStatus().reemitOnExpiry()
}
