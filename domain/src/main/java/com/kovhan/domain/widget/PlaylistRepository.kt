package com.kovhan.domain.widget

import com.kovhan.core.models.widget.Playlist
import com.kovhan.core.models.widget.PlaylistSource
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun observeAll(): Flow<List<Playlist>>

    suspend fun getAll(): List<Playlist>

    suspend fun getById(id: String): Playlist?

    suspend fun create(name: String, sources: List<PlaylistSource>): String

    suspend fun update(playlist: Playlist)

    suspend fun rename(id: String, name: String)

    suspend fun delete(id: String)
}
