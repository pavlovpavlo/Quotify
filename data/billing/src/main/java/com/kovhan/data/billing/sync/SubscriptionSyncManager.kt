package com.kovhan.data.billing.sync

import androidx.fragment.app.FragmentActivity
import com.kovhan.core.models.billing.isEntitled
import com.kovhan.core.ui.activity.ActivityRequired
import com.kovhan.domain.ai.SubscriptionRepository
import com.kovhan.domain.billing.BillingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SubscriptionSyncManager @Inject constructor(
    private val billingRepository: BillingRepository,
    private val subscriptionRepository: SubscriptionRepository,
) : ActivityRequired {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var syncJob: Job? = null
    private var lastSyncAt = 0L

    override fun onCreated(activity: FragmentActivity) = Unit

    override fun onStarted() {
        if (syncJob?.isActive == true) return

        syncJob = scope.launch {
            billingRepository.observePurchases()

            val now = System.currentTimeMillis()
            val entitled = runCatching {
                subscriptionRepository.observeStatus().first().isEntitled()
            }.getOrDefault(false)
            if (entitled && now - lastSyncAt < MIN_SYNC_INTERVAL_MS) return@launch
            lastSyncAt = now

            if (runCatching { billingRepository.connect() }.getOrDefault(false)) {
                runCatching { billingRepository.refreshPurchases() }
                    .onFailure { Timber.w(it, "Play purchase refresh failed") }
            }
            runCatching { subscriptionRepository.refresh() }
                .onFailure { Timber.w(it, "Subscription status refresh failed") }
        }
    }

    override fun onStopped() = Unit

    override fun onDestroyed() = Unit

    private companion object {
        const val MIN_SYNC_INTERVAL_MS = 60_000L
    }
}
