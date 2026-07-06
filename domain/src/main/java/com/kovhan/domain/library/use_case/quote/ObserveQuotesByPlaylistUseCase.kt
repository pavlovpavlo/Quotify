package com.kovhan.domain.library.use_case.quote

import com.kovhan.core.models.Quote
import com.kovhan.core.models.QuoteFilter
import com.kovhan.core.models.QuotePlaylist
import com.kovhan.domain.library.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveQuotesByPlaylistUseCase @Inject constructor(
    private val repository: QuoteRepository,
) {
    operator fun invoke(playlist: QuotePlaylist): Flow<List<Quote>> =
        repository.observeFiltered(QuoteFilter(playlist = playlist))
}
