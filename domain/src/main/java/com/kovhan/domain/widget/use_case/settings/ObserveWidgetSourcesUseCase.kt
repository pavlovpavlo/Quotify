package com.kovhan.domain.widget.use_case.settings

import com.kovhan.core.models.widget.PlaylistWithCount
import com.kovhan.core.models.widget.WidgetSourceCounts
import com.kovhan.domain.library.use_case.quote.ObserveQuotesUseCase
import com.kovhan.domain.widget.quoteCount
import com.kovhan.domain.widget.use_case.playlist.ObservePlaylistsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Emits the selectable widget sources — the built-in "all" / "favourites"
 * counts plus every user playlist with its resolved quote count.
 */
class ObserveWidgetSourcesUseCase @Inject constructor(
    private val observePlaylists: ObservePlaylistsUseCase,
    private val observeQuotes: ObserveQuotesUseCase,
) {
    operator fun invoke(): Flow<WidgetSourceCounts> =
        combine(observePlaylists(), observeQuotes()) { playlists, quotes ->
            WidgetSourceCounts(
                allCount = quotes.size,
                favouritesCount = quotes.count { it.isFavourite },
                playlists = playlists.map { PlaylistWithCount(it, it.quoteCount(quotes)) },
            )
        }
}
