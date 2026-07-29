package com.kovhan.core.models.widget

/**
 * A user-defined, named set of quote sources that can feed the home-screen
 * widget. Quotes are resolved live from [sources], so new quotes added to a
 * picked structure (folder, tag, author, book) always flow in automatically.
 */
data class Playlist(
    val id: String,
    val name: String,
    val sources: List<PlaylistSource> = emptyList(),
)
