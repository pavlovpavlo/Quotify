package com.kovhan.data.library.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Reads the subscription status from the `premium` map on the `users/{uid}` document.
 * Billing writes this field elsewhere; here we only consume it.
 */
@Singleton
class SubscriptionRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {
    suspend fun isSubscribed(): Boolean {
        val uid = auth.currentUser?.uid ?: return false
        val snapshot = firestore.collection(USERS_COLLECTION).document(uid).get().await()
        val premium = snapshot.get(FIELD_PREMIUM) as? Map<*, *> ?: return false
        val isActive = premium[FIELD_IS_ACTIVE] as? Boolean ?: false
        val status = premium[FIELD_STATUS] as? String
        val expiresAt = (premium[FIELD_EXPIRES_AT] as? Number)?.toLong()
        val notExpired = expiresAt == null || expiresAt > System.currentTimeMillis()
        return isActive && (status == null || status == STATUS_ACTIVE) && notExpired
    }

    private companion object {
        const val FIELD_PREMIUM = "premium"
        const val FIELD_IS_ACTIVE = "isActive"
        const val FIELD_STATUS = "status"
        const val FIELD_EXPIRES_AT = "expiresAt"
        const val STATUS_ACTIVE = "active"
    }
}
