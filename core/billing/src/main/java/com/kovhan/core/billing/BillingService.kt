package com.kovhan.core.billing

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryProductDetailsResult
import com.android.billingclient.api.QueryPurchasesParams
import com.kovhan.core.billing.mapper.toPremiumProduct
import com.kovhan.core.billing.model.PlayPurchase
import com.kovhan.core.models.billing.PremiumProduct
import com.kovhan.core.models.billing.PurchaseFlowFailure
import com.kovhan.core.ui.activity.ActivityRequired
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class BillingService @Inject constructor(
    @ApplicationContext context: Context,
) : ActivityRequired {

    private var activity: Activity? = null

    private val offerTokenToProduct = ConcurrentHashMap<String, ProductDetails>()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val purchasesUpdatedListener = PurchasesUpdatedListener { result, purchases ->
        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
            emitPurchases(purchases)
            return@PurchasesUpdatedListener
        }
        Timber.i("Billing: флоу оплати закрито без покупки, code=%d", result.responseCode)
        when (result.responseCode) {
            BillingClient.BillingResponseCode.USER_CANCELED ->
                _purchaseFlowFailures.tryEmit(PurchaseFlowFailure.CANCELLED)

            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                _purchaseFlowFailures.tryEmit(PurchaseFlowFailure.ALREADY_OWNED)
                scope.launch { runCatching { refreshActiveSubscriptions() } }
            }

            else -> _purchaseFlowFailures.tryEmit(PurchaseFlowFailure.FAILED)
        }
    }

    private val billingClient = BillingClient.newBuilder(context)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .build(),
        )
        .enableAutoServiceReconnection()
        .build()

    private val _purchases = MutableSharedFlow<PlayPurchase>(
        replay = 8,
        extraBufferCapacity = 8,
    )
    val purchases: SharedFlow<PlayPurchase> = _purchases.asSharedFlow()

    private val _purchaseFlowFailures =
        MutableSharedFlow<PurchaseFlowFailure>(extraBufferCapacity = 4)
    val purchaseFlowFailures: SharedFlow<PurchaseFlowFailure> =
        _purchaseFlowFailures.asSharedFlow()

    suspend fun connect(): Boolean {
        if (billingClient.isReady) return true
        return suspendCancellableCoroutine { continuation ->
            billingClient.startConnection(
                object : BillingClientStateListener {
                    override fun onBillingSetupFinished(result: BillingResult) {
                        if (continuation.isActive) {
                            continuation.resume(
                                result.responseCode == BillingClient.BillingResponseCode.OK,
                            )
                        }
                    }

                    override fun onBillingServiceDisconnected() = Unit
                },
            )
        }
    }

    suspend fun queryPremiumProduct(productId: String): PremiumProduct? {
        if (!connect()) return null

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productId)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),
                ),
            )
            .build()

        val result = queryProductDetails(params) ?: return null
        val details = result.productDetailsList.firstOrNull() ?: return null
        cacheOffers(details)
        return details.toPremiumProduct()
    }

    private suspend fun queryProductDetails(
        params: QueryProductDetailsParams,
    ): QueryProductDetailsResult? = suspendCancellableCoroutine { continuation ->
        billingClient.queryProductDetailsAsync(params) { result, productDetails ->
            if (!continuation.isActive) return@queryProductDetailsAsync
            continuation.resume(
                productDetails.takeIf {
                    result.responseCode == BillingClient.BillingResponseCode.OK
                },
            )
        }
    }

    fun launchPurchase(offerToken: String): Boolean {
        val currentActivity = activity ?: return false
        val details = offerTokenToProduct[offerToken] ?: return false

        val productParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(details)
            .setOfferToken(offerToken)
            .build()

        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productParams))
            .build()

        val result = billingClient.launchBillingFlow(currentActivity, flowParams)
        return result.responseCode == BillingClient.BillingResponseCode.OK
    }

    suspend fun refreshActiveSubscriptions() {
        if (!connect()) return
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()
        emitPurchases(queryPurchases(params))
    }

    private suspend fun queryPurchases(
        params: QueryPurchasesParams,
    ): List<Purchase> = suspendCancellableCoroutine { continuation ->
        billingClient.queryPurchasesAsync(params) { result, purchases ->
            if (!continuation.isActive) return@queryPurchasesAsync
            continuation.resume(
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    purchases
                } else {
                    emptyList()
                },
            )
        }
    }

    private fun cacheOffers(details: ProductDetails) {
        details.subscriptionOfferDetails?.forEach { offer ->
            offerTokenToProduct[offer.offerToken] = details
        }
    }

    private fun emitPurchases(purchases: List<Purchase?>?) {
        val all = purchases?.filterNotNull().orEmpty()
        Timber.i("Billing: Play віддав %d покупок", all.size)
        all.forEach { purchase ->
            val productId = purchase.products.firstOrNull()
            if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) {
                Timber.w(
                    "Billing: покупку %s пропущено, state=%d",
                    productId,
                    purchase.purchaseState,
                )
                return@forEach
            }
            if (productId == null) {
                Timber.w("Billing: покупка без productId, пропущено")
                return@forEach
            }
            val delivered = _purchases.tryEmit(
                PlayPurchase(
                    productId = productId,
                    purchaseToken = purchase.purchaseToken,
                    isAcknowledged = purchase.isAcknowledged,
                ),
            )
            Timber.i("Billing: покупку %s передано на верифікацію=%b", productId, delivered)
        }
    }

    override fun onCreated(activity: FragmentActivity) {
        this.activity = activity
    }

    override fun onStarted() = Unit

    override fun onStopped() = Unit

    override fun onDestroyed() {
        activity = null
    }
}
