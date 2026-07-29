package com.kovhan.data.library.local.widget

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey val id: String,
    val name: String,
)

@Entity(
    tableName = "playlist_sources",
    primaryKeys = ["playlistId", "type", "refId"],
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("playlistId")],
)
data class PlaylistSourceEntity(
    val playlistId: String,
    val type: String,
    val refId: String,
)

data class PlaylistWithSources(
    @Embedded val playlist: PlaylistEntity,
    @Relation(parentColumn = "id", entityColumn = "playlistId")
    val sources: List<PlaylistSourceEntity>,
)
