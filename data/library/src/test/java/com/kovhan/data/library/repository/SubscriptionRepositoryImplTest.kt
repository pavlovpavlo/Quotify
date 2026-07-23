package com.kovhan.data.library.repository

import com.kovhan.data.library.local.library.SubscriptionDao
import com.kovhan.data.library.local.library.SubscriptionEntity
import com.kovhan.data.library.remote.SubscriptionRemoteDataSource
import com.kovhan.domain.connectivity.ConnectivityRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
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
        coEvery { remote.isSubscribed() } returns true
        val cached = slot<SubscriptionEntity>()

        assertTrue(repository.isSubscribed())

        coVerify { cacheDao.set(capture(cached)) }
        assertTrue(cached.captured.isSubscribed)
    }

    @Test
    @DisplayName("online falls back to the cache when the remote read fails")
    fun onlineFallsBackToCache() = runTest {
        coEvery { connectivity.isOnline() } returns true
        coEvery { remote.isSubscribed() } throws RuntimeException("boom")
        coEvery { cacheDao.get() } returns true

        assertTrue(repository.isSubscribed())
    }

    @Test
    @DisplayName("offline returns the cached status without hitting the network")
    fun offlineReadsCache() = runTest {
        coEvery { connectivity.isOnline() } returns false
        coEvery { cacheDao.get() } returns true

        assertTrue(repository.isSubscribed())

        coVerify(exactly = 0) { remote.isSubscribed() }
    }

    @Test
    @DisplayName("offline with no cached status defaults to not subscribed")
    fun offlineDefaultsFalse() = runTest {
        coEvery { connectivity.isOnline() } returns false
        coEvery { cacheDao.get() } returns null

        assertFalse(repository.isSubscribed())
    }
}
