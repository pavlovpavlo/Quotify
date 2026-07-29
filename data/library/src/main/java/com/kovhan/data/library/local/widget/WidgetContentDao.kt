package com.kovhan.data.library.local.widget

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface WidgetContentDao {

    // region Snapshot

    @Query("SELECT quoteId FROM widget_quotes ORDER BY position")
    fun observeSnapshotIds(): Flow<List<String>>

    @Query("SELECT quoteId FROM widget_quotes ORDER BY position")
    suspend fun getSnapshotIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshot(items: List<WidgetQuoteEntity>)

    @Query("DELETE FROM widget_quotes")
    suspend fun clearSnapshot()

    @Query("DELETE FROM widget_seen WHERE quoteId NOT IN (SELECT quoteId FROM widget_quotes)")
    suspend fun pruneSeen()

    @Transaction
    suspend fun replaceSnapshot(items: List<WidgetQuoteEntity>) {
        clearSnapshot()
        insertSnapshot(items)
        pruneSeen()
    }

    // endregion

    // region Seen

    @Query("SELECT quoteId FROM widget_seen")
    suspend fun getSeenIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeen(entity: WidgetSeenEntity)

    @Query("DELETE FROM widget_seen")
    suspend fun clearSeen()

    // endregion

    // region State

    @Query("SELECT * FROM widget_state WHERE id = 0")
    suspend fun getState(): WidgetStateEntity?

    @Query("SELECT * FROM widget_state WHERE id = 0")
    fun observeState(): Flow<WidgetStateEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setState(entity: WidgetStateEntity)

    // endregion
}
