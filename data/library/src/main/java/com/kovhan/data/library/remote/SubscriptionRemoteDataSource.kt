package com.kovhan.data.library.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kovhan.core.models.billing.SubscriptionStatus
import com.kovhan.core.models.billing.isEntitled
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Reads the subscription status from the `premium` map on the `users/{uid}` document.
 * The Cloudflare Worker writes that map; here we only consume it.
 */
@Singleton
class SubscriptionRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {
    suspend fun getStatus(): SubscriptionStatus {
        val uid = auth.currentUser?.uid ?: return SubscriptionStatus.None
        val snapshot = firestore.collection(USERS_COLLECTION).document(uid).get().await()
        val premium = snapshot.get(FIELD_PREMIUM) as? Map<*, *> ?: return SubscriptionStatus.None

        return SubscriptionStatus(
            isActive = premium[FIELD_IS_ACTIVE] as? Boolean ?: false,
            status = premium[FIELD_STATUS] as? String,
            expiresAt = (premium[FIELD_EXPIRES_AT] as? Number)?.toLong(),
            autoRenewing = premium[FIELD_AUTO_RENEWING] as? Boolean ?: false,
            productId = premium[FIELD_PRODUCT_ID] as? String,
            basePlanId = (premium[FIELD_BASE_PLAN_ID] as? String)?.takeIf { it.isNotBlank() },
            startedAt = (premium[FIELD_STARTED_AT] as? Number)?.toLong()?.takeIf { it > 0L },
        )
    }

    suspend fun isSubscribed(): Boolean = getStatus().isEntitled()

    private companion object {
        const val FIELD_PREMIUM = "premium"
        const val FIELD_IS_ACTIVE = "isActive"
        const val FIELD_STATUS = "status"
        const val FIELD_EXPIRES_AT = "expiresAt"
        const val FIELD_AUTO_RENEWING = "autoRenewing"
        const val FIELD_PRODUCT_ID = "productId"
        const val FIELD_BASE_PLAN_ID = "basePlanId"
        const val FIELD_STARTED_AT = "startedAt"
    }
}
