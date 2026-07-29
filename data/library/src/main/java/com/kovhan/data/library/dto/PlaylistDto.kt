package com.kovhan.data.library.dto

/**
 * Firestore representation of a widget playlist. Sources are stored as separate
 * per-type id arrays (grouped from the flat [com.kovhan.core.models.widget.PlaylistSource]
 * list) so they read naturally in the console and can be queried per type.
 */
data class PlaylistDto(
    val id: String = "",
    val name: String = "",
    val quoteIds: List<String> = emptyList(),
    val folderIds: List<String> = emptyList(),
    val tagIds: List<String> = emptyList(),
    val bookIds: List<String> = emptyList(),
    val authorIds: List<String> = emptyList(),
)
