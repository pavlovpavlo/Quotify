package com.kovhan.core.billing.mapper

import com.android.billingclient.api.ProductDetails
import com.kovhan.core.models.billing.PremiumOffer
import com.kovhan.core.models.billing.PremiumProduct
import java.time.Period
import java.time.format.DateTimeParseException

fun ProductDetails.toPremiumProduct(): PremiumProduct = PremiumProduct(
    productId = productId,
    offers = subscriptionOfferDetails?.map { it.toPremiumOffer() },
)

fun ProductDetails.SubscriptionOfferDetails.toPremiumOffer(): PremiumOffer {
    val phases = pricingPhases.pricingPhaseList
    val recurring = phases.lastOrNull()
    val trial = phases.firstOrNull { it.priceAmountMicros == 0L }
    val intro = phases.firstOrNull { it.priceAmountMicros > 0L && it !== recurring }

    return PremiumOffer(
        offerToken = offerToken,
        offerId = offerId,
        basePlanId = basePlanId,
        formattedPrice = recurring?.formattedPrice.orEmpty(),
        priceAmountMicros = recurring?.priceAmountMicros ?: 0L,
        priceCurrencyCode = recurring?.priceCurrencyCode.orEmpty(),
        billingPeriod = recurring?.billingPeriod.orEmpty(),
        freeTrialDays = trial?.billingPeriod?.toDaysOrNull(),
        introFormattedPrice = intro?.formattedPrice,
        introAmountMicros = intro?.priceAmountMicros,
        introCycleCount = intro?.billingCycleCount,
    )
}

private fun String.toDaysOrNull(): Int? = try {
    val period = Period.parse(this)
    val days = period.years * 365 + period.months * 30 + period.days
    days.takeIf { it > 0 }
} catch (_: DateTimeParseException) {
    null
}
