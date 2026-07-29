package com.kovhan.domain.widget.use_case.playlist

import com.kovhan.core.models.widget.Playlist
import com.kovhan.domain.widget.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePlaylistsUseCase @Inject constructor(
    private val repository: PlaylistRepository,
) {
    operator fun invoke(): Flow<List<Playlist>> = repository.observeAll()
}
