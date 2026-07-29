package com.kovhan.core.models.widget

/** A [Playlist] paired with how many library quotes currently resolve into it. */
data class PlaylistWithCount(
    val playlist: Playlist,
    val quoteCount: Int,
)
