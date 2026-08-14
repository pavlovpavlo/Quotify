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
    val priceAmountMicros: Long,
    val priceCurrencyCode: String,
    val billingPeriod: String,
    val freeTrialDays: Int?,
    val introFormattedPrice: String? = null,
    val introAmountMicros: Long? = null,
    val introCycleCount: Int? = null,
)
