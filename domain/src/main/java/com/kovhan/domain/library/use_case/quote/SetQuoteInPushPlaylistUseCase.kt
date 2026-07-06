package com.kovhan.domain.library.use_case.quote

import com.kovhan.domain.library.QuoteRepository
import javax.inject.Inject

class SetQuoteInPushPlaylistUseCase @Inject constructor(
    private val repository: QuoteRepository,
) {
    suspend operator fun invoke(id: String, added: Boolean) =
        repository.setInPushPlaylist(id, added)
}
