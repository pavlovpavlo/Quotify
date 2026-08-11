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
import com.kovhan.core.ui.activity.ActivityRequired
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

/**
 * Тонка обгортка над Play Billing Library. Нічого не вирішує про доступ:
 * лише емітить покупки в [purchases], а верифікує їх уже репозиторій через бекенд.
 */
@Singleton
class BillingService @Inject constructor(
    @ApplicationContext context: Context,
) : ActivityRequired {

    private var activity: Activity? = null

    /**
     * launchBillingFlow вимагає ProductDetails, але домен оперує лише offerToken,
     * тому тримаємо тут результат останнього queryPremiumProduct.
     */
    private val offerTokenToProduct = ConcurrentHashMap<String, ProductDetails>()

    private val purchasesUpdatedListener = PurchasesUpdatedListener { result, purchases ->
        if (result.responseCode == BillingClient.BillingResponseCode.OK) emitPurchases(purchases)
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

    // replay, бо колектор репозиторію стартує в окремій корутині: без нього
    // покупки, віддані refreshActiveSubscriptions одразу після observePurchases,
    // емітяться в потік без підписників і мовчки зникають.
    private val _purchases = MutableSharedFlow<PlayPurchase>(
        replay = 8,
        extraBufferCapacity = 8,
    )
    val purchases: SharedFlow<PlayPurchase> = _purchases.asSharedFlow()

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

    /** @return false, якщо offerToken невідомий (не було queryPremiumProduct) або немає Activity. */
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

    /** Відновлення покупок: Play віддає активні підписки цього акаунта на пристрої. */
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
                // PENDING тут не помилка, але й доступу не дає — інакше це виглядає
                // як "купив, а нічого не сталося".
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
