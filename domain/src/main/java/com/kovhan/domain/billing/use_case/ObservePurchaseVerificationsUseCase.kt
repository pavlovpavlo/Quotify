package com.kovhan.domain.billing.use_case

import com.kovhan.core.models.Outcome
import com.kovhan.core.models.billing.BillingError
import com.kovhan.core.models.billing.SubscriptionStatus
import com.kovhan.domain.billing.BillingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePurchaseVerificationsUseCase @Inject constructor(
    private val repository: BillingRepository,
) {
    operator fun invoke(): Flow<Outcome<SubscriptionStatus, BillingError>> = repository.verifications
}
