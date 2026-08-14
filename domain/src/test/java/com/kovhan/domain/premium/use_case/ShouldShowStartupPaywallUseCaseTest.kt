package com.kovhan.domain.premium.use_case

import com.kovhan.domain.billing.use_case.IsSubscribedUseCase
import com.kovhan.domain.premium.PaywallPromptRepository
import com.kovhan.domain.premium.PremiumLimits
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.concurrent.TimeUnit

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ShouldShowStartupPaywallUseCase")
class ShouldShowStartupPaywallUseCaseTest {

    private val isSubscribed: IsSubscribedUseCase = mockk()
    private val repository: PaywallPromptRepository = mockk(relaxed = true)
    private val useCase = ShouldShowStartupPaywallUseCase(isSubscribed, repository)

    private val intervalMs = TimeUnit.DAYS.toMillis(PremiumLimits.STARTUP_PAYWALL_INTERVAL_DAYS)

    @Test
    @DisplayName("subscriber never sees the startup paywall")
    fun neverForSubscriber() = runTest {
        coEvery { isSubscribed() } returns true

        assertFalse(useCase())
    }

    @Test
    @DisplayName("first launch only seeds the timestamp")
    fun firstLaunchSeedsOnly() = runTest {
        coEvery { isSubscribed() } returns false
        coEvery { repository.lastShownAt() } returns 0L

        assertFalse(useCase())
        coVerify { repository.markShown(any()) }
    }

    @Test
    @DisplayName("shown again once the interval has elapsed")
    fun showsAfterInterval() = runTest {
        coEvery { isSubscribed() } returns false
        coEvery { repository.lastShownAt() } returns System.currentTimeMillis() - intervalMs - 1_000L

        assertTrue(useCase())
    }

    @Test
    @DisplayName("stays hidden inside the interval")
    fun hiddenInsideInterval() = runTest {
        coEvery { isSubscribed() } returns false
        coEvery { repository.lastShownAt() } returns System.currentTimeMillis() - intervalMs + 60_000L

        assertFalse(useCase())
    }
}
