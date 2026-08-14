package com.kovhan.domain.billing

import com.kovhan.core.models.Outcome
import com.kovhan.core.models.billing.BillingError
import com.kovhan.core.models.billing.PremiumProduct
import com.kovhan.core.models.billing.PurchaseFlowFailure
import com.kovhan.core.models.billing.SubscriptionStatus
import kotlinx.coroutines.flow.Flow

interface BillingRepository {

    val verifications: Flow<Outcome<SubscriptionStatus, BillingError>>

    val purchaseFlowFailures: Flow<PurchaseFlowFailure>

    suspend fun connect(): Boolean

    suspend fun getPremiumProduct(productId: String): PremiumProduct?

    fun launchPurchase(offerToken: String): Boolean

    fun observePurchases()

    suspend fun refreshPurchases()
}
