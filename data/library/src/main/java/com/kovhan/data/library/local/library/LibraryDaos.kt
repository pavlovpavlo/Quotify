package com.kovhan.data.library.local.library

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    @Query("SELECT * FROM quotes")
    fun observeAll(): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM quotes")
    suspend fun getAll(): List<QuoteEntity>

    @Query("SELECT * FROM quotes WHERE id = :id")
    suspend fun getById(id: String): QuoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: QuoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<QuoteEntity>)

    @Query("DELETE FROM quotes WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("UPDATE quotes SET inPushPlaylist = :added WHERE id = :id")
    suspend fun setInPushPlaylist(id: String, added: Boolean)

    @Query("UPDATE quotes SET inWidgetPlaylist = :added WHERE id = :id")
    suspend fun setInWidgetPlaylist(id: String, added: Boolean)

    @Query("DELETE FROM quotes")
    suspend fun clear()

    @Transaction
    suspend fun replaceAll(entities: List<QuoteEntity>) {
        clear()
        insertAll(entities)
    }
}

@Dao
interface CollectionDao {

    @Query("SELECT * FROM collections")
    fun observeAll(): Flow<List<CollectionEntity>>

    @Query("SELECT * FROM collections")
    suspend fun getAll(): List<CollectionEntity>

    @Query("SELECT * FROM collections WHERE id = :id")
    suspend fun getById(id: String): CollectionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: CollectionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<CollectionEntity>)

    @Query("DELETE FROM collections WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM collections")
    suspend fun clear()

    @Transaction
    suspend fun replaceAll(entities: List<CollectionEntity>) {
        clear()
        insertAll(entities)
    }
}

@Dao
interface SavedAuthorDao {

    @Query("SELECT * FROM saved_authors")
    fun observeAll(): Flow<List<SavedAuthorEntity>>

    @Query("SELECT * FROM saved_authors")
    suspend fun getAll(): List<SavedAuthorEntity>

    @Query("SELECT * FROM saved_authors WHERE id = :id")
    suspend fun getById(id: String): SavedAuthorEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: SavedAuthorEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<SavedAuthorEntity>)

    @Query("DELETE FROM saved_authors WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM saved_authors")
    suspend fun clear()

    @Transaction
    suspend fun replaceAll(entities: List<SavedAuthorEntity>) {
        clear()
        insertAll(entities)
    }
}

@Dao
interface SavedBookDao {

    @Query("SELECT * FROM saved_books")
    fun observeAll(): Flow<List<SavedBookEntity>>

    @Query("SELECT * FROM saved_books")
    suspend fun getAll(): List<SavedBookEntity>

    @Query("SELECT * FROM saved_books WHERE id = :id")
    suspend fun getById(id: String): SavedBookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: SavedBookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<SavedBookEntity>)

    @Query("DELETE FROM saved_books WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM saved_books")
    suspend fun clear()

    @Transaction
    suspend fun replaceAll(entities: List<SavedBookEntity>) {
        clear()
        insertAll(entities)
    }
}

@Dao
interface SavedTagDao {

    @Query("SELECT * FROM saved_tags")
    fun observeAll(): Flow<List<SavedTagEntity>>

    @Query("SELECT * FROM saved_tags")
    suspend fun getAll(): List<SavedTagEntity>

    @Query("SELECT * FROM saved_tags WHERE id = :id")
    suspend fun getById(id: String): SavedTagEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: SavedTagEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<SavedTagEntity>)

    @Query("DELETE FROM saved_tags WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM saved_tags")
    suspend fun clear()

    @Transaction
    suspend fun replaceAll(entities: List<SavedTagEntity>) {
        clear()
        insertAll(entities)
    }
}

@Dao
interface PendingOperationDao {

    @Query("SELECT * FROM pending_operations ORDER BY createdAt ASC")
    suspend fun getAllOrdered(): List<PendingOperationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PendingOperationEntity)

    @Query("DELETE FROM pending_operations WHERE `key` = :key")
    suspend fun deleteByKey(key: String)

    @Query("DELETE FROM pending_operations")
    suspend fun clear()

    @Query("SELECT COUNT(*) FROM pending_operations")
    suspend fun count(): Int
}

@Dao
interface SubscriptionDao {

    @Query("SELECT isSubscribed FROM subscription_status WHERE id = :id")
    suspend fun get(id: Int = SubscriptionEntity.SINGLE_ROW_ID): Boolean?

    @Query("SELECT * FROM subscription_status WHERE id = :id")
    suspend fun getStatus(id: Int = SubscriptionEntity.SINGLE_ROW_ID): SubscriptionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun set(entity: SubscriptionEntity)
}
