package com.kovhan.data.library.local.daily

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_selection")
data class DailySelectionEntity(
    @PrimaryKey val id: Int = SINGLE_ROW_ID,
    val quoteId: String? = null,
    val epochDay: Long? = null,
    val dismissedEpochDay: Long? = null,
) {
    companion object {
        const val SINGLE_ROW_ID = 0
    }
}
