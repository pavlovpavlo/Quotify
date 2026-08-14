package com.kovhan.core.models.billing

data class SubscriptionStatus(
    val isActive: Boolean,
    val status: String?,
    val expiresAt: Long?,
    val autoRenewing: Boolean,
    val productId: String?,
    /** base plan із Play Console (weekly/monthly/yearly); null, поки бекенд його не записав. */
    val basePlanId: String? = null,
    /** Коли підписку оформили вперше, ms epoch; null якщо невідомо. */
    val startedAt: Long? = null,
) {
    companion object {
        const val ACTIVE = "active"
        const val CANCELED = "canceled"
        const val IN_GRACE_PERIOD = "in_grace_period"
        const val ON_HOLD = "on_hold"
        const val PAUSED = "paused"
        const val EXPIRED = "expired"

        val ENTITLED_STATUSES = setOf(ACTIVE, CANCELED, IN_GRACE_PERIOD)

        val None = SubscriptionStatus(
            isActive = false,
            status = null,
            expiresAt = null,
            autoRenewing = false,
            productId = null,
        )
    }
}

fun SubscriptionStatus.isEntitled(): Boolean {
    val notExpired = expiresAt == null || expiresAt > System.currentTimeMillis()
    val entitledStatus = status == null || status in SubscriptionStatus.ENTITLED_STATUSES
    return isActive && entitledStatus && notExpired
}
