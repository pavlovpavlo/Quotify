package com.kovhan.domain.billing

import com.kovhan.core.models.Outcome
import com.kovhan.core.models.billing.BillingError
import com.kovhan.core.models.billing.PremiumProduct
import com.kovhan.core.models.billing.SubscriptionStatus
import kotlinx.coroutines.flow.Flow

interface BillingRepository {

    /** Результати серверної верифікації — і після оплати, і після відновлення покупок. */
    val verifications: Flow<Outcome<SubscriptionStatus, BillingError>>

    suspend fun connect(): Boolean

    suspend fun getPremiumProduct(productId: String): PremiumProduct?

    /** @return false, якщо оплату не вдалось запустити (немає Activity або невідомий offer). */
    fun launchPurchase(offerToken: String): Boolean

    /** Запускає конвеєр «покупка → верифікація на бекенді → оновлення статусу». Ідемпотентно. */
    fun observePurchases()

    /** Перепитує Google Play про активні підписки цього пристрою. */
    suspend fun refreshPurchases()
}
