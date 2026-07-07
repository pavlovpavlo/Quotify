package com.kovhan.data.library.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kovhan.data.library.local.daily.DailyQuoteDao
import com.kovhan.data.library.local.daily.DailyQuoteEntity
import com.kovhan.data.library.local.daily.DailySeenDao
import com.kovhan.data.library.local.daily.DailySeenEntity
import com.kovhan.data.library.local.daily.DailySelectionDao
import com.kovhan.data.library.local.daily.DailySelectionEntity

@Database(
    entities = [DailyQuoteEntity::class, DailySelectionEntity::class, DailySeenEntity::class],
    version = 4,
    exportSchema = false,
)
abstract class QuotifyDatabase : RoomDatabase() {
    abstract fun dailyQuoteDao(): DailyQuoteDao
    abstract fun dailySelectionDao(): DailySelectionDao
    abstract fun dailySeenDao(): DailySeenDao
}
