package com.kovhan.data.library.local.widget

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Transaction
    @Query("SELECT * FROM playlists")
    fun observeAll(): Flow<List<PlaylistWithSources>>

    @Transaction
    @Query("SELECT * FROM playlists")
    suspend fun getAllWithSources(): List<PlaylistWithSources>

    @Transaction
    @Query("SELECT * FROM playlists WHERE id = :id")
    suspend fun getById(id: String): PlaylistWithSources?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPlaylist(entity: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylists(entities: List<PlaylistEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSources(sources: List<PlaylistSourceEntity>)

    @Query("DELETE FROM playlist_sources WHERE playlistId = :id")
    suspend fun clearSources(id: String)

    @Query("UPDATE playlists SET name = :name WHERE id = :id")
    suspend fun rename(id: String, name: String)

    @Query("DELETE FROM playlists WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM playlists")
    suspend fun clear()

    @Transaction
    suspend fun upsertWithSources(
        playlist: PlaylistEntity,
        sources: List<PlaylistSourceEntity>,
    ) {
        upsertPlaylist(playlist)
        clearSources(playlist.id)
        insertSources(sources)
    }

    /** Replaces the whole local playlist store — used by the remote refresh. */
    @Transaction
    suspend fun replaceAll(
        playlists: List<PlaylistEntity>,
        sources: List<PlaylistSourceEntity>,
    ) {
        clear() // cascades to playlist_sources via the foreign key
        insertPlaylists(playlists)
        insertSources(sources)
    }
}
