package com.kovhan.data.library.local.daily

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_seen")
data class DailySeenEntity(
    @PrimaryKey val quoteId: String,
)
