package com.kovhan.core.billing.mapper

import com.android.billingclient.api.ProductDetails
import com.kovhan.core.models.billing.PremiumOffer
import com.kovhan.core.models.billing.PremiumProduct

fun ProductDetails.toPremiumProduct(): PremiumProduct{
    return PremiumProduct(
        productId = this.productId,
        offers = this.subscriptionOfferDetails?.map { it.toPremiumOffer() }
    )
}

fun ProductDetails.SubscriptionOfferDetails.toPremiumOffer() = PremiumOffer(
    offerToken = this.offerToken,
    offerId = this.offerId,
    basePlanId = this.basePlanId,
    formattedPrice = pricingPhases.pricingPhaseList.firstOrNull()
        ?.formattedPrice?: ""
)