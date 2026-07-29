package com.kovhan.core.models.widget

/** Quote counts for every source the widget-settings screen can offer. */
data class WidgetSourceCounts(
    val allCount: Int = 0,
    val favouritesCount: Int = 0,
    val playlists: List<PlaylistWithCount> = emptyList(),
)
