package com.kovhan.domain.billing.use_case

import com.kovhan.core.models.billing.isEntitled
import com.kovhan.domain.ai.SubscriptionRepository
import com.kovhan.domain.billing.reemitOnExpiry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Єдине джерело «чи є преміум» для UI. Екрани підписуються на цей потік
 * і не роблять власних запитів до Firestore — синком займається
 * `SubscriptionSyncManager` при старті застосунку та поверненні з фону.
 */
class ObserveIsSubscribedUseCase @Inject constructor(
    private val repository: SubscriptionRepository,
) {
    operator fun invoke(): Flow<Boolean> = repository.observeStatus()
        .reemitOnExpiry()
        .map { it.isEntitled() }
        .distinctUntilChanged()
}
