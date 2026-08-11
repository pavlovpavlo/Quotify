package com.kovhan.data.library.repository

import com.kovhan.core.models.billing.SubscriptionStatus
import com.kovhan.data.library.local.library.SubscriptionDao
import com.kovhan.data.library.local.library.SubscriptionEntity
import com.kovhan.data.library.remote.SubscriptionRemoteDataSource
import com.kovhan.domain.connectivity.ConnectivityRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("SubscriptionRepositoryImpl")
class SubscriptionRepositoryImplTest {

    private val remote: SubscriptionRemoteDataSource = mockk()
    private val connectivity: ConnectivityRepository = mockk()
    private val cacheDao: SubscriptionDao = mockk(relaxed = true)

    private lateinit var repository: SubscriptionRepositoryImpl

    @BeforeEach
    fun setUp() {
        repository = SubscriptionRepositoryImpl(remote, connectivity, cacheDao)
    }

    @Test
    @DisplayName("online reads the live status and caches it")
    fun onlineReadsAndCaches() = runTest {
        coEvery { connectivity.isOnline() } returns true
        coEvery { remote.getStatus() } returns activeStatus()
        val cached = slot<SubscriptionEntity>()

        assertTrue(repository.isSubscribed())

        coVerify { cacheDao.set(capture(cached)) }
        assertTrue(cached.captured.isSubscribed)
        assertEquals(SubscriptionStatus.ACTIVE, cached.captured.status)
    }

    @Test
    @DisplayName("grace period still counts as entitled")
    fun gracePeriodIsEntitled() = runTest {
        coEvery { connectivity.isOnline() } returns true
        coEvery { remote.getStatus() } returns activeStatus(status = SubscriptionStatus.IN_GRACE_PERIOD)

        assertTrue(repository.isSubscribed())
    }

    @Test
    @DisplayName("an expired subscription is not entitled even when isActive is true")
    fun expiredIsNotEntitled() = runTest {
        coEvery { connectivity.isOnline() } returns true
        coEvery { remote.getStatus() } returns activeStatus(
            expiresAt = System.currentTimeMillis() - 1_000,
        )

        assertFalse(repository.isSubscribed())
    }

    @Test
    @DisplayName("online falls back to the cache when the remote read fails")
    fun onlineFallsBackToCache() = runTest {
        coEvery { connectivity.isOnline() } returns true
        coEvery { remote.getStatus() } throws RuntimeException("boom")
        coEvery { cacheDao.getStatus() } returns cachedEntity()

        assertTrue(repository.isSubscribed())
    }

    @Test
    @DisplayName("offline returns the cached status without hitting the network")
    fun offlineReadsCache() = runTest {
        coEvery { connectivity.isOnline() } returns false
        coEvery { cacheDao.getStatus() } returns cachedEntity()

        assertTrue(repository.isSubscribed())

        coVerify(exactly = 0) { remote.getStatus() }
    }

    @Test
    @DisplayName("offline with no cached status defaults to not subscribed")
    fun offlineDefaultsFalse() = runTest {
        coEvery { connectivity.isOnline() } returns false
        coEvery { cacheDao.getStatus() } returns null

        assertFalse(repository.isSubscribed())
    }

    private fun activeStatus(
        status: String = SubscriptionStatus.ACTIVE,
        expiresAt: Long = System.currentTimeMillis() + DAY_MS,
    ) = SubscriptionStatus(
        isActive = true,
        status = status,
        expiresAt = expiresAt,
        autoRenewing = true,
        productId = "premium",
    )

    private fun cachedEntity() = SubscriptionEntity(
        isSubscribed = true,
        status = SubscriptionStatus.ACTIVE,
        expiresAt = System.currentTimeMillis() + DAY_MS,
        autoRenewing = true,
        productId = "premium",
    )

    private companion object {
        const val DAY_MS = 24 * 60 * 60 * 1000L
    }
}
