package com.kovhan.domain.widget.use_case.content

import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.models.widget.WidgetSource
import com.kovhan.domain.library.use_case.quote.ObserveQuotesUseCase
import com.kovhan.domain.widget.matches
import com.kovhan.domain.widget.use_case.playlist.ObservePlaylistsUseCase
import com.kovhan.domain.widget.use_case.settings.ObserveWidgetSettingsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Resolves the currently-selected widget source into the live list of library
 * quote ids that belong on the widget. Reactive: re-emits whenever the settings,
 * the library, or a playlist definition changes — so structures (folders, tags,
 * authors, books) always resolve to their current members.
 */
class ResolveWidgetQuoteIdsUseCase @Inject constructor(
    private val observeSettings: ObserveWidgetSettingsUseCase,
    private val observeQuotes: ObserveQuotesUseCase,
    private val observePlaylists: ObservePlaylistsUseCase,
) {
    operator fun invoke(): Flow<List<String>> =
        combine(
            observeSettings(),
            observeQuotes(),
            observePlaylists(),
        ) { settings, quotes, playlists ->
            when (val source = settings.source) {
                WidgetSource.All -> quotes.map { it.id }

                WidgetSource.Favorites ->
                    quotes.filter { it.collectionId == SavedCollection.FAVOURITES_ID }.map { it.id }

                is WidgetSource.Playlist -> {
                    val playlist = playlists.firstOrNull { it.id == source.playlistId }
                    playlist?.let { pl -> quotes.filter { pl.matches(it) }.map { it.id } }.orEmpty()
                }
            }
        }
}
