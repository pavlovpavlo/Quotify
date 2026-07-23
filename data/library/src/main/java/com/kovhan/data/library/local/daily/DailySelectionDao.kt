package com.kovhan.data.library.local.daily

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DailySelectionDao {

    @Query("SELECT * FROM daily_selection WHERE id = :id")
    suspend fun get(id: Int = DailySelectionEntity.SINGLE_ROW_ID): DailySelectionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun set(entity: DailySelectionEntity)
}
