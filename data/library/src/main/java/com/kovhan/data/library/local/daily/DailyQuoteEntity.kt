package com.kovhan.data.library.local.daily

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_quotes")
data class DailyQuoteEntity(
    @PrimaryKey val id: String,
    val textEn: String,
    val textUk: String,
    val authorEn: String?,
    val authorUk: String?,
    val bookEn: String?,
    val bookUk: String?,
)
