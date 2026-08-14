package com.kovhan.domain.billing.use_case

import com.kovhan.core.models.billing.PremiumOffer
import com.kovhan.domain.billing.BillingProducts
import javax.inject.Inject

class GetSpecialOfferUseCase @Inject constructor(
    private val getPremiumProduct: GetPremiumProductUseCase,
) {
    suspend operator fun invoke(): PremiumOffer? {
        val product = runCatching { getPremiumProduct() }.getOrNull() ?: return null
        return product.offers?.firstOrNull {
            it.offerId == BillingProducts.OFFER_YEARLY_SPECIAL && it.offerToken.isNotBlank()
        }
    }
}
