package com.kovhan.data.library.repository

import com.kovhan.core.models.billing.SubscriptionStatus
import com.kovhan.core.models.billing.isEntitled
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

    override suspend fun isSubscribed(): Boolean = getStatus().isEntitled()

    override suspend fun getStatus(): SubscriptionStatus {
        if (connectivity.isOnline()) {
            return runCatching { refresh() }.getOrElse { cachedStatus() }
        }
        return cachedStatus()
    }

    override suspend fun refresh(): SubscriptionStatus {
        val status = remote.getStatus()
        cacheDao.set(
            SubscriptionEntity(
                isSubscribed = status.isEntitled(),
                status = status.status,
                expiresAt = status.expiresAt,
                autoRenewing = status.autoRenewing,
                productId = status.productId,
            ),
        )
        return status
    }

    private suspend fun cachedStatus(): SubscriptionStatus {
        val cached = cacheDao.getStatus() ?: return SubscriptionStatus.None
        return SubscriptionStatus(
            isActive = cached.isSubscribed,
            status = cached.status,
            expiresAt = cached.expiresAt,
            autoRenewing = cached.autoRenewing,
            productId = cached.productId,
        )
    }
}
