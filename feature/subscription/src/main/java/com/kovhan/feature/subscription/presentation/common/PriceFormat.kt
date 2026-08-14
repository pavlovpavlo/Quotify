package com.kovhan.feature.subscription.presentation.common

import com.kovhan.core.models.billing.PremiumOffer
import com.kovhan.core.ui.mapper.CurrencySymbolMapper
import com.kovhan.domain.billing.BillingProducts
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

private const val MICROS_PER_UNIT = 1_000_000.0
private const val MONTHS_PER_YEAR = 12
private const val PERCENT = 100
private const val FRACTION_DIGITS = 2

internal fun PremiumOffer.displayPrice(locale: Locale): String =
    formatMoney(priceAmountMicros, priceCurrencyCode, locale) ?: formattedPrice

internal fun PremiumOffer.introDisplayPriceOrNull(locale: Locale): String? {
    val micros = introAmountMicros ?: return null
    return formatMoney(micros, priceCurrencyCode, locale) ?: introFormattedPrice
}

internal fun PremiumOffer.monthlyEquivalentOrNull(locale: Locale): String? =
    perMonthOrNull(priceAmountMicros, locale)

internal fun PremiumOffer.introMonthlyOrNull(locale: Locale): String? {
    val micros = introAmountMicros ?: return null
    return perMonthOrNull(micros, locale)
}

internal fun PremiumOffer.discountPercentOrNull(): Int? {
    val intro = introAmountMicros ?: return null
    if (priceAmountMicros <= 0L || intro >= priceAmountMicros) return null
    val percent = (priceAmountMicros - intro).toDouble() / priceAmountMicros * PERCENT
    return percent.roundToInt().takeIf { it > 0 }
}

private fun PremiumOffer.perMonthOrNull(micros: Long, locale: Locale): String? {
    if (basePlanId != BillingProducts.BASE_PLAN_YEARLY) return null
    return formatMoney(micros / MONTHS_PER_YEAR, priceCurrencyCode, locale)
}

internal fun formatMoney(micros: Long, currencyCode: String, locale: Locale): String? {
    if (micros <= 0L || currencyCode.isBlank()) return null

    val amount = micros / MICROS_PER_UNIT
    val formatted = NumberFormat.getNumberInstance(locale).apply {
        maximumFractionDigits = FRACTION_DIGITS
        minimumFractionDigits = FRACTION_DIGITS
    }.format(amount)

    return CurrencySymbolMapper.symbolOf(currencyCode, locale) + formatted
}
