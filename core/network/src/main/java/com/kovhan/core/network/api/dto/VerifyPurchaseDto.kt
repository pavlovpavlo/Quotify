package com.kovhan.core.network.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyPurchaseRequest(
    @SerialName("productId") val productId: String,
    @SerialName("purchaseToken") val purchaseToken: String,
)

@Serializable
data class VerifyPurchaseResponse(
    @SerialName("entitled") val entitled: Boolean,
    @SerialName("status") val status: String,
    @SerialName("productId") val productId: String? = null,
    @SerialName("expiresAt") val expiresAt: Long = 0L,
    @SerialName("autoRenewing") val autoRenewing: Boolean = false,
)

@Serializable
data class ApiErrorResponse(
    @SerialName("error") val error: String,
    @SerialName("message") val message: String? = null,
)
