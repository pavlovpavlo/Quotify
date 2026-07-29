package com.kovhan.core.models.widget

/**
 * The quote source feeding the home-screen widget: one of the built-in system
 * sources ([All] / [Favorites]) or a specific user [Playlist].
 */
sealed interface WidgetSource {
    data object All : WidgetSource

    data object Favorites : WidgetSource

    data class Playlist(val playlistId: String) : WidgetSource
}
