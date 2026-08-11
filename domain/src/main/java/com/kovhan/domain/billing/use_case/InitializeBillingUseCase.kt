package com.kovhan.domain.billing.use_case

import com.kovhan.domain.billing.BillingRepository
import javax.inject.Inject

/**
 * Викликається один раз на старті застосунку. Піднімає з'єднання з Play,
 * запускає конвеєр верифікації та перепитує активні підписки — саме це
 * замінює RTDN: продовження й повернення коштів підхоплюються при вході.
 */
class InitializeBillingUseCase @Inject constructor(
    private val repository: BillingRepository,
) {
    suspend operator fun invoke() {
        repository.observePurchases()
        if (repository.connect()) repository.refreshPurchases()
    }
}
