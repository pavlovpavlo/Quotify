package com.kovhan.data.billing.sync

import androidx.fragment.app.FragmentActivity
import com.kovhan.core.ui.activity.ActivityRequired
import com.kovhan.domain.ai.SubscriptionRepository
import com.kovhan.domain.billing.BillingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
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
        val now = System.currentTimeMillis()
        if (now - lastSyncAt < MIN_SYNC_INTERVAL_MS) return
        if (syncJob?.isActive == true) return

        lastSyncAt = now
        syncJob = scope.launch {
            billingRepository.observePurchases()
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
