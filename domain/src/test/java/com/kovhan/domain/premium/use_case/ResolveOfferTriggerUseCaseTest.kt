package com.kovhan.domain.premium.use_case

import com.kovhan.core.models.billing.SubscriptionStatus
import com.kovhan.domain.ai.SubscriptionRepository
import com.kovhan.domain.premium.OfferPromptRepository
import com.kovhan.domain.premium.OfferTrigger
import com.kovhan.domain.premium.PremiumLimits
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.concurrent.TimeUnit

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ResolveOfferTriggerUseCase")
class ResolveOfferTriggerUseCaseTest {

    private val subscriptionRepository: SubscriptionRepository = mockk()
    private val repository: OfferPromptRepository = mockk(relaxed = true)
    private val useCase = ResolveOfferTriggerUseCase(subscriptionRepository, repository)

    private val now = 1_800_000_000_000L
    private val tenureMs = TimeUnit.DAYS.toMillis(PremiumLimits.OFFER_TENURE_DAYS)

    private fun status(status: String?, isActive: Boolean) = SubscriptionStatus(
        isActive = isActive,
        status = status,
        expiresAt = now + tenureMs,
        autoRenewing = status == SubscriptionStatus.ACTIVE,
        productId = "premium",
    )

    private fun expiredStatus(status: String) = SubscriptionStatus(
        isActive = true,
        status = status,
        expiresAt = System.currentTimeMillis() - 1_000L,
        autoRenewing = false,
        productId = "premium",
    )

    @Test
    @DisplayName("active subscriber is never offered anything and keeps restarting the countdown")
    fun activeSubscriberSeesNothing() = runTest {
        coEvery { subscriptionRepository.observeStatus() } returns
            flowOf(status(SubscriptionStatus.ACTIVE, isActive = true))

        assertNull(useCase(now))
        coVerify { repository.rememberFreeSince(now) }
        coVerify { repository.clearShown(OfferTrigger.TENURE) }
    }

    @Test
    @DisplayName("a canceled but still paid-for subscription is left alone")
    fun canceledWhileStillEntitled() = runTest {
        coEvery { subscriptionRepository.observeStatus() } returns
            flowOf(status(SubscriptionStatus.CANCELED, isActive = true))

        assertNull(useCase(now))
    }

    @Test
    @DisplayName("a canceled subscription triggers the win-back offer once it has run out")
    fun canceledTriggersWinBack() = runTest {
        coEvery { subscriptionRepository.observeStatus() } returns
            flowOf(expiredStatus(SubscriptionStatus.CANCELED))
        coEvery { repository.isShown(OfferTrigger.CANCELED) } returns false

        assertEquals(OfferTrigger.CANCELED, useCase(now))
    }

    @Test
    @DisplayName("the win-back offer is shown only once")
    fun canceledOnlyOnce() = runTest {
        coEvery { subscriptionRepository.observeStatus() } returns
            flowOf(expiredStatus(SubscriptionStatus.CANCELED))
        coEvery { repository.isShown(OfferTrigger.CANCELED) } returns true

        assertNull(useCase(now))
    }

    @Test
    @DisplayName("first launch without a subscription only seeds the date, it offers nothing")
    fun firstLaunchSeedsDateOnly() = runTest {
        coEvery { subscriptionRepository.observeStatus() } returns flowOf(SubscriptionStatus.None)
        coEvery { repository.freeSinceAt() } returns 0L
        coEvery { repository.isShown(OfferTrigger.TENURE) } returns false

        assertNull(useCase(now))
        coVerify { repository.rememberFreeSince(now) }
    }

    @Test
    @DisplayName("nothing fires before the tenure mark")
    fun quietBeforeTenure() = runTest {
        coEvery { subscriptionRepository.observeStatus() } returns flowOf(SubscriptionStatus.None)
        coEvery { repository.freeSinceAt() } returns now - tenureMs + 60_000L
        coEvery { repository.isShown(OfferTrigger.TENURE) } returns false

        assertNull(useCase(now))
    }

    @Test
    @DisplayName("the tenure offer fires once enough days without a subscription have passed")
    fun tenureFiresAfterEnoughDays() = runTest {
        coEvery { subscriptionRepository.observeStatus() } returns flowOf(SubscriptionStatus.None)
        coEvery { repository.freeSinceAt() } returns now - tenureMs - 1_000L
        coEvery { repository.isShown(OfferTrigger.TENURE) } returns false

        assertEquals(OfferTrigger.TENURE, useCase(now))
    }

    @Test
    @DisplayName("the tenure offer is shown only once per subscription-free stretch")
    fun tenureOnlyOnce() = runTest {
        coEvery { subscriptionRepository.observeStatus() } returns flowOf(SubscriptionStatus.None)
        coEvery { repository.freeSinceAt() } returns now - tenureMs - 1_000L
        coEvery { repository.isShown(OfferTrigger.TENURE) } returns true

        assertNull(useCase(now))
    }

    @Test
    @DisplayName("a failing status read keeps the offer hidden")
    fun statusFailureStaysQuiet() = runTest {
        coEvery { subscriptionRepository.observeStatus() } throws IllegalStateException("offline")

        assertNull(useCase(now))
    }
}
