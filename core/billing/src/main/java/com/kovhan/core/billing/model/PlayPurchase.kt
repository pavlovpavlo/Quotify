package com.kovhan.core.billing.model

data class PlayPurchase(
    val productId: String,
    val purchaseToken: String,
    val isAcknowledged: Boolean,
)
