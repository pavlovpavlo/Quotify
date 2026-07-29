package com.kovhan.core.models.widget

/**
 * A single element picked into a [Playlist]. Either a concrete quote or a
 * "structure" (folder / tag / book / author) whose current quotes are all
 * resolved into the playlist.
 */
data class PlaylistSource(
    val type: PlaylistSourceType,
    val refId: String,
)
