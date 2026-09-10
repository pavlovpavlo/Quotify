package com.kovhan.data.library.repository

import com.kovhan.core.models.billing.SubscriptionStatus
import com.kovhan.core.models.billing.isEntitled
import com.kovhan.data.library.local.library.SubscriptionDao
import com.kovhan.data.library.local.library.SubscriptionEntity
import com.kovhan.data.library.remote.SubscriptionRemoteDataSource
import com.kovhan.domain.ai.SubscriptionRepository
import com.kovhan.domain.connectivity.ConnectivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
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

    /**
     * Гейти безкоштовного плану смикають це на кожному збереженні, тож мережевий
     * раунд-трип блокував би кнопку «Зберегти». Кеш оновлюють [refresh] на
     * фореграунді та верифікація покупки; у мережу йдемо, лише поки кешу немає.
     */
    override suspend fun isSubscribed(): Boolean {
        val cached = cacheDao.getStatus() ?: return getStatus().isEntitled()
        return cached.toStatus().isEntitled()
    }

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
                basePlanId = status.basePlanId,
                startedAt = status.startedAt,
            ),
        )
        return status
    }

    override fun observeStatus(): Flow<SubscriptionStatus> = cacheDao.observeStatus()
        .map { it.toStatus() }
        .distinctUntilChanged()

    private suspend fun cachedStatus(): SubscriptionStatus = cacheDao.getStatus().toStatus()

    private fun SubscriptionEntity?.toStatus(): SubscriptionStatus {
        if (this == null) return SubscriptionStatus.None
        return SubscriptionStatus(
            isActive = isSubscribed,
            status = status,
            expiresAt = expiresAt,
            autoRenewing = autoRenewing,
            productId = productId,
            basePlanId = basePlanId,
            startedAt = startedAt,
        )
    }
}
