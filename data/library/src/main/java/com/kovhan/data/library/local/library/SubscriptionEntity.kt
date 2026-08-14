package com.kovhan.data.library.local.library

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Single-row cache of the last known subscription status, so the offline gate and
 * the management UI keep working without a network round-trip.
 */
@Entity(tableName = "subscription_status")
data class SubscriptionEntity(
    @PrimaryKey val id: Int = SINGLE_ROW_ID,
    val isSubscribed: Boolean = false,
    val status: String? = null,
    val expiresAt: Long? = null,
    val autoRenewing: Boolean = false,
    val productId: String? = null,
    val basePlanId: String? = null,
    val startedAt: Long? = null,
) {
    companion object {
        const val SINGLE_ROW_ID = 0
    }
}
