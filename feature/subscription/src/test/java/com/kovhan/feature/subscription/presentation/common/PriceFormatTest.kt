package com.kovhan.feature.subscription.presentation.common

import com.kovhan.core.models.billing.PremiumOffer
import com.kovhan.domain.billing.BillingProducts
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.Locale

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("PremiumOffer.monthlyEquivalentOrNull")
class PriceFormatTest {

    private val locale = Locale.forLanguageTag("uk-UA")

    @Test
    @DisplayName("divides the yearly price into twelve months, keeping cents")
    fun derivesMonthlyPrice() {
        val formatted = offer(
            basePlanId = BillingProducts.BASE_PLAN_YEARLY,
            micros = 790_000_000L,
        ).monthlyEquivalentOrNull(locale)

        assertNotNull(formatted)
        assertTrue(formatted!!.contains("65"), "expected 65 in \"$formatted\"")
        assertTrue(formatted.contains("83"), "expected 83 cents in \"$formatted\"")
    }

    @Test
    @DisplayName("a whole amount still shows two decimals")
    fun keepsTrailingZeros() {
        val formatted = offer(
            basePlanId = BillingProducts.BASE_PLAN_YEARLY,
            micros = 790_000_000L,
            currency = "USD",
        ).displayPrice(Locale.US)

        assertEquals("$790.00", formatted)
    }

    @Test
    @DisplayName("keeps cents when the derived price is under ten")
    fun keepsCentsForSmallAmounts() {
        val formatted = offer(
            basePlanId = BillingProducts.BASE_PLAN_YEARLY,
            micros = 19_990_000L,
            currency = "USD",
        ).monthlyEquivalentOrNull(Locale.US)

        assertNotNull(formatted)
        assertTrue(formatted!!.contains("1.67"), "expected 1.67 in \"$formatted\"")
    }

    @Test
    @DisplayName("only the yearly plan gets a derived monthly price")
    fun onlyYearly() {
        assertNull(
            offer(basePlanId = BillingProducts.BASE_PLAN_MONTHLY, micros = 79_000_000L)
                .monthlyEquivalentOrNull(locale),
        )
        assertNull(
            offer(basePlanId = BillingProducts.BASE_PLAN_WEEKLY, micros = 39_000_000L)
                .monthlyEquivalentOrNull(locale),
        )
    }

    @Test
    @DisplayName("falls back to null when Play gave no currency or amount")
    fun missingPlayData() {
        assertNull(
            offer(basePlanId = BillingProducts.BASE_PLAN_YEARLY, micros = 0L)
                .monthlyEquivalentOrNull(locale),
        )
        assertNull(
            offer(basePlanId = BillingProducts.BASE_PLAN_YEARLY, micros = 790_000_000L, currency = "")
                .monthlyEquivalentOrNull(locale),
        )
    }

    private fun offer(
        basePlanId: String,
        micros: Long,
        currency: String = "UAH",
    ) = PremiumOffer(
        offerToken = "token",
        basePlanId = basePlanId,
        offerId = null,
        formattedPrice = "",
        priceAmountMicros = micros,
        priceCurrencyCode = currency,
        billingPeriod = "P1Y",
        freeTrialDays = null,
    )
}
