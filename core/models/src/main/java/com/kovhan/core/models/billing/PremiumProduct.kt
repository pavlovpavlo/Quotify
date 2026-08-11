package com.kovhan.core.models.billing

data class PremiumProduct(
    val productId: String,
    val offers: List<PremiumOffer>?,
)

data class PremiumOffer(
    val offerToken: String,
    val basePlanId: String,
    val offerId: String?,
    val formattedPrice: String,
)