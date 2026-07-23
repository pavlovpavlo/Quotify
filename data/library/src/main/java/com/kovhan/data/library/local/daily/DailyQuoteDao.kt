package com.kovhan.data.library.local.daily

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface DailyQuoteDao {

    @Query("SELECT * FROM daily_quotes")
    suspend fun getAll(): List<DailyQuoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<DailyQuoteEntity>)

    @Query("DELETE FROM daily_quotes")
    suspend fun clear()

    @Transaction
    suspend fun replaceAll(entities: List<DailyQuoteEntity>) {
        clear()
        insertAll(entities)
    }
}
