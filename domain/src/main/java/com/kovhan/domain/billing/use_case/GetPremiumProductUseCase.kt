package com.kovhan.domain.billing.use_case

import com.kovhan.core.models.billing.PremiumProduct
import com.kovhan.domain.billing.BillingProducts
import com.kovhan.domain.billing.BillingRepository
import javax.inject.Inject

class GetPremiumProductUseCase @Inject constructor(
    private val repository: BillingRepository,
) {
    suspend operator fun invoke(
        productId: String = BillingProducts.PREMIUM,
    ): PremiumProduct? = repository.getPremiumProduct(productId)
}
