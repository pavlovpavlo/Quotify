package com.kovhan.feature.subscription.presentation.common

import com.kovhan.core.models.billing.PremiumOffer

internal fun PremiumOffer.discountMicros(): Long =
    introAmountMicros?.let { (priceAmountMicros - it).coerceAtLeast(0L) } ?: 0L

internal fun PremiumOffer.hasBenefit(): Boolean =
    (freeTrialDays ?: 0) > 0 || discountMicros() > 0L

internal val byOfferBenefit: Comparator<PremiumOffer> = compareBy(
    { offer -> offer.freeTrialDays ?: 0 },
    { offer -> offer.discountMicros() },
)
