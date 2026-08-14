package com.kovhan.data.billing.remote

import com.google.firebase.auth.FirebaseAuth
import com.kovhan.core.models.Outcome
import com.kovhan.core.models.billing.BillingError
import com.kovhan.core.models.billing.SubscriptionStatus
import com.kovhan.core.network.ApiError
import com.kovhan.core.network.api.BillingApi
import com.kovhan.core.network.api.dto.VerifyPurchaseResponse
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Серверна верифікація покупки. Клієнту не можна довіряти рішення про доступ,
 * тому статус видає бекенд: він питає Google Play і сам пише `premium` у Firestore.
 */
@Singleton
class BillingDataSource @Inject constructor(
    private val api: BillingApi,
    private val auth: FirebaseAuth,
) {
    suspend fun verifyPlayPurchase(
        productId: String,
        purchaseToken: String,
    ): Outcome<SubscriptionStatus, BillingError> {
        val idToken = currentIdToken()
        if (idToken == null) {
            return Outcome.Failure(BillingError.NOT_AUTHENTICATED)
        }

        return when (val result = api.verifyPurchase(idToken, productId, purchaseToken)) {
            is Outcome.Success -> Outcome.Success(result.data.toSubscriptionStatus())
            is Outcome.Failure -> {
                Timber.e(
                    "Verify purchase failed: code=%s http=%s message=%s",
                    result.error.code,
                    result.error.httpStatus,
                    result.error.message,
                )
                Outcome.Failure(result.error.toBillingError())
            }
        }
    }

    private suspend fun currentIdToken(): String? = runCatching {
        auth.currentUser?.getIdToken(false)?.await()?.token
    }.getOrNull()

    private fun VerifyPurchaseResponse.toSubscriptionStatus() = SubscriptionStatus(
        isActive = entitled,
        status = status,
        expiresAt = expiresAt.takeIf { it > 0L },
        autoRenewing = autoRenewing,
        productId = productId,
    )

    private fun ApiError.toBillingError(): BillingError = when (code) {
        ApiError.NETWORK_UNAVAILABLE -> BillingError.NETWORK
        "unauthenticated", "invalid_token" -> BillingError.NOT_AUTHENTICATED
        "token_owned_by_another_account" -> BillingError.TOKEN_OWNED_BY_ANOTHER_ACCOUNT
        "unknown_purchase_token", "invalid_argument" -> BillingError.UNKNOWN_PURCHASE
        "play_api_failed", "firestore_write_failed", "google_auth_failed", "internal" ->
            BillingError.BACKEND
        else -> BillingError.UNKNOWN
    }
}
