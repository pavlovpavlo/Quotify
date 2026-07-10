package com.kovhan.data.library.local.library

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Single-row cache of the last known subscription status, so the offline gate can read it. */
@Entity(tableName = "subscription_status")
data class SubscriptionEntity(
    @PrimaryKey val id: Int = SINGLE_ROW_ID,
    val isSubscribed: Boolean = false,
) {
    companion object {
        const val SINGLE_ROW_ID = 0
    }
}
