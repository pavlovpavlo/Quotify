package com.kovhan.data.library.mapper

import com.kovhan.core.models.widget.Playlist
import com.kovhan.core.models.widget.PlaylistSource
import com.kovhan.core.models.widget.PlaylistSourceType
import com.kovhan.data.library.dto.PlaylistDto
import com.kovhan.data.library.local.widget.PlaylistEntity
import com.kovhan.data.library.local.widget.PlaylistSourceEntity
import com.kovhan.data.library.local.widget.PlaylistWithSources

fun PlaylistWithSources.toDomain() = Playlist(
    id = playlist.id,
    name = playlist.name,
    sources = sources.mapNotNull { it.toDomain() },
)

fun PlaylistSourceEntity.toDomain(): PlaylistSource? {
    val sourceType = runCatching { PlaylistSourceType.valueOf(type) }.getOrNull() ?: return null
    return PlaylistSource(type = sourceType, refId = refId)
}

fun Playlist.toEntity() = PlaylistEntity(
    id = id,
    name = name,
)

fun Playlist.toSourceEntities() = sources.map { source ->
    PlaylistSourceEntity(
        playlistId = id,
        type = source.type.name,
        refId = source.refId,
    )
}

// region Firestore DTO

private fun PlaylistWithSources.refIdsOf(type: PlaylistSourceType): List<String> =
    sources.filter { it.type == type.name }.map { it.refId }

fun PlaylistWithSources.toDto() = PlaylistDto(
    id = playlist.id,
    name = playlist.name,
    quoteIds = refIdsOf(PlaylistSourceType.QUOTE),
    folderIds = refIdsOf(PlaylistSourceType.FOLDER),
    tagIds = refIdsOf(PlaylistSourceType.TAG),
    bookIds = refIdsOf(PlaylistSourceType.BOOK),
    authorIds = refIdsOf(PlaylistSourceType.AUTHOR),
)

fun PlaylistDto.toPlaylistEntity() = PlaylistEntity(id = id, name = name)

fun PlaylistDto.toSourceEntities(): List<PlaylistSourceEntity> = buildList {
    fun addAll(type: PlaylistSourceType, ids: List<String>) =
        ids.forEach { add(PlaylistSourceEntity(playlistId = id, type = type.name, refId = it)) }
    addAll(PlaylistSourceType.QUOTE, quoteIds)
    addAll(PlaylistSourceType.FOLDER, folderIds)
    addAll(PlaylistSourceType.TAG, tagIds)
    addAll(PlaylistSourceType.BOOK, bookIds)
    addAll(PlaylistSourceType.AUTHOR, authorIds)
}

// endregion
