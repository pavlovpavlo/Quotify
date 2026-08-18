package com.kovhan.data.billing.repository

import com.kovhan.core.billing.BillingService
import com.kovhan.core.models.Outcome
import com.kovhan.core.models.billing.BillingError
import com.kovhan.core.models.billing.PremiumProduct
import com.kovhan.core.models.billing.PurchaseFlowFailure
import com.kovhan.core.models.billing.SubscriptionStatus
import com.kovhan.data.billing.remote.BillingDataSource
import com.kovhan.domain.ai.SubscriptionRepository
import com.kovhan.domain.billing.BillingRepository
import com.kovhan.domain.billing.BillingWaits
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillingRepositoryImpl @Inject constructor(
    private val billingService: BillingService,
    private val billingDataSource: BillingDataSource,
    private val subscriptionRepository: SubscriptionRepository,
) : BillingRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val observing = AtomicBoolean(false)

    private val _verifications =
        MutableSharedFlow<Outcome<SubscriptionStatus, BillingError>>(extraBufferCapacity = 8)
    override val verifications: SharedFlow<Outcome<SubscriptionStatus, BillingError>> =
        _verifications.asSharedFlow()

    override val purchaseFlowFailures: SharedFlow<PurchaseFlowFailure> =
        billingService.purchaseFlowFailures

    override suspend fun connect(): Boolean = billingService.connect()

    override suspend fun getPremiumProduct(productId: String): PremiumProduct? =
        billingService.queryPremiumProduct(productId)

    override fun launchPurchase(offerToken: String): Boolean =
        billingService.launchPurchase(offerToken)


    override fun observePurchases() {
        if (!observing.compareAndSet(false, true)) return
        scope.launch {
            billingService.purchases.collect { purchase ->
                val result = verifyWithRetry(purchase.productId, purchase.purchaseToken)
                if (result is Outcome.Success) {
                    runCatching { subscriptionRepository.refresh() }
                }
                _verifications.tryEmit(result)
            }
        }
    }

    private suspend fun verifyWithRetry(
        productId: String,
        purchaseToken: String,
    ): Outcome<SubscriptionStatus, BillingError> {
        var attempt = 0
        while (true) {
            Timber.i("Verify purchase: надсилаємо %s на бекенд, спроба %d", productId, attempt + 1)
            val result = billingDataSource.verifyPlayPurchase(productId, purchaseToken)

            if (result is Outcome.Success) {
                Timber.i(
                    "Verify purchase: бекенд відповів entitled=%b status=%s",
                    result.data.isActive,
                    result.data.status,
                )
                return result
            }

            val error = (result as Outcome.Failure).error
            attempt++
            if (error !in RETRYABLE_ERRORS || attempt >= BillingWaits.MAX_VERIFY_ATTEMPTS) {
                Timber.w("Verify purchase: здаємось після %d спроб, error=%s", attempt, error)
                return result
            }
            delay(BillingWaits.VERIFY_RETRY_STEP_MS * attempt)
        }
    }

    override suspend fun refreshPurchases() {
        billingService.refreshActiveSubscriptions()
    }

    private companion object {
        val RETRYABLE_ERRORS = setOf(
            BillingError.NETWORK,
            BillingError.BACKEND,
            BillingError.NOT_AUTHENTICATED,
            BillingError.UNKNOWN,
        )
    }
}
