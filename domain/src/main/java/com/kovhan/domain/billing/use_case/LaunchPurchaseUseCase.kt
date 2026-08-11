package com.kovhan.domain.billing.use_case

import com.kovhan.domain.billing.BillingRepository
import javax.inject.Inject

class LaunchPurchaseUseCase @Inject constructor(
    private val repository: BillingRepository,
) {
    /** @return false, якщо системний діалог оплати не відкрився. */
    operator fun invoke(offerToken: String): Boolean = repository.launchPurchase(offerToken)
}
