package com.kovhan.domain.billing.use_case

import com.kovhan.core.models.billing.PurchaseFlowFailure
import com.kovhan.domain.billing.BillingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePurchaseFlowFailuresUseCase @Inject constructor(
    private val repository: BillingRepository,
) {
    operator fun invoke(): Flow<PurchaseFlowFailure> = repository.purchaseFlowFailures
}
