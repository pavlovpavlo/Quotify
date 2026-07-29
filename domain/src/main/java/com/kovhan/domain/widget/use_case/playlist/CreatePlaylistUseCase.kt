package com.kovhan.domain.widget.use_case.playlist

import com.kovhan.core.models.widget.PlaylistSource
import com.kovhan.domain.widget.PlaylistRepository
import javax.inject.Inject

class CreatePlaylistUseCase @Inject constructor(
    private val repository: PlaylistRepository,
) {
    suspend operator fun invoke(
        name: String,
        sources: List<PlaylistSource>,
    ): String = repository.create(name.trim(), sources)
}
