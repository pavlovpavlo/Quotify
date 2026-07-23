package com.kovhan.data.library.repository

import com.kovhan.data.library.local.library.SubscriptionDao
import com.kovhan.data.library.local.library.SubscriptionEntity
import com.kovhan.data.library.remote.SubscriptionRemoteDataSource
import com.kovhan.domain.ai.SubscriptionRepository
import com.kovhan.domain.connectivity.ConnectivityRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Online: reads the live subscription status from Firestore and caches it locally.
 * Offline: returns the last cached status, so the offline gate can honour subscriptions.
 */
@Singleton
class SubscriptionRepositoryImpl @Inject constructor(
    private val remote: SubscriptionRemoteDataSource,
    private val connectivity: ConnectivityRepository,
    private val cacheDao: SubscriptionDao,
) : SubscriptionRepository {

    override suspend fun isSubscribed(): Boolean {
        if (connectivity.isOnline()) {
            return runCatching {
                val subscribed = remote.isSubscribed()
                cacheDao.set(SubscriptionEntity(isSubscribed = subscribed))
                subscribed
            }.getOrElse { cachedStatus() }
        }
        return cachedStatus()
    }

    private suspend fun cachedStatus(): Boolean = cacheDao.get() ?: false
}
