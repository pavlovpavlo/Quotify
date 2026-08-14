package com.kovhan.core.billing.mapper

import com.android.billingclient.api.ProductDetails
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ProductDetails.SubscriptionOfferDetails.toPremiumOffer")
class PremiumProductMapperTest {

    @Test
    @DisplayName("takes the recurring price from the last pricing phase, not the trial")
    fun readsRecurringPhase() {
        val offer = offerDetails(
            phases = listOf(
                phase(micros = 0L, formatted = "Free", period = "P5D"),
                phase(micros = 790_000_000L, formatted = "₴790.00", period = "P1Y"),
            ),
        ).toPremiumOffer()

        assertEquals("₴790.00", offer.formattedPrice)
        assertEquals(790_000_000L, offer.priceAmountMicros)
        assertEquals("UAH", offer.priceCurrencyCode)
        assertEquals("P1Y", offer.billingPeriod)
    }

    @Test
    @DisplayName("reads the trial length from the zero-priced phase")
    fun readsTrialDays() {
        val offer = offerDetails(
            phases = listOf(
                phase(micros = 0L, formatted = "Free", period = "P5D"),
                phase(micros = 790_000_000L, formatted = "₴790.00", period = "P1Y"),
            ),
        ).toPremiumOffer()

        assertEquals(5, offer.freeTrialDays)
    }

    @Test
    @DisplayName("converts a one-week trial into days")
    fun convertsWeeklyTrial() {
        val offer = offerDetails(
            phases = listOf(
                phase(micros = 0L, formatted = "Free", period = "P1W"),
                phase(micros = 79_000_000L, formatted = "₴79.00", period = "P1M"),
            ),
        ).toPremiumOffer()

        assertEquals(7, offer.freeTrialDays)
    }

    @Test
    @DisplayName("an offer without a trial reports no trial days")
    fun noTrial() {
        val offer = offerDetails(
            phases = listOf(phase(micros = 39_000_000L, formatted = "₴39.00", period = "P1W")),
        ).toPremiumOffer()

        assertNull(offer.freeTrialDays)
        assertEquals("₴39.00", offer.formattedPrice)
    }

    @Test
    @DisplayName("reads the discounted intro phase without losing the full renewal price")
    fun readsIntroPrice() {
        val offer = offerDetails(
            phases = listOf(
                phase(micros = 395_000_000L, formatted = "₴395.00", period = "P1Y", cycles = 1),
                phase(micros = 790_000_000L, formatted = "₴790.00", period = "P1Y"),
            ),
        ).toPremiumOffer()

        assertEquals("₴395.00", offer.introFormattedPrice)
        assertEquals(395_000_000L, offer.introAmountMicros)
        assertEquals(1, offer.introCycleCount)
        assertEquals("₴790.00", offer.formattedPrice)
    }

    @Test
    @DisplayName("a plain offer reports no intro price")
    fun noIntroPrice() {
        val offer = offerDetails(
            phases = listOf(phase(micros = 790_000_000L, formatted = "₴790.00", period = "P1Y")),
        ).toPremiumOffer()

        assertNull(offer.introFormattedPrice)
        assertNull(offer.introAmountMicros)
    }

    private fun offerDetails(
        phases: List<ProductDetails.PricingPhase>,
    ): ProductDetails.SubscriptionOfferDetails {
        val pricingPhases = mockk<ProductDetails.PricingPhases> {
            every { pricingPhaseList } returns phases
        }
        return mockk {
            every { offerToken } returns "token"
            every { basePlanId } returns "yearly"
            every { offerId } returns null
            every { this@mockk.pricingPhases } returns pricingPhases
        }
    }

    private fun phase(
        micros: Long,
        formatted: String,
        period: String,
        currency: String = "UAH",
        cycles: Int = 0,
    ): ProductDetails.PricingPhase = mockk {
        every { priceAmountMicros } returns micros
        every { formattedPrice } returns formatted
        every { billingPeriod } returns period
        every { priceCurrencyCode } returns currency
        every { billingCycleCount } returns cycles
    }
}
