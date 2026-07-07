package com.kovhan.data.library.local.daily

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DailySeenDao {

    @Query("SELECT quoteId FROM daily_seen")
    suspend fun getAllIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: DailySeenEntity)

    @Query("DELETE FROM daily_seen")
    suspend fun clear()
}
