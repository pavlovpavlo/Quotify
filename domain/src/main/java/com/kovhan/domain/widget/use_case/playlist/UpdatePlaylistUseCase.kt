package com.kovhan.domain.widget.use_case.playlist

import com.kovhan.core.models.widget.Playlist
import com.kovhan.domain.widget.PlaylistRepository
import javax.inject.Inject

class UpdatePlaylistUseCase @Inject constructor(
    private val repository: PlaylistRepository,
) {
    suspend operator fun invoke(playlist: Playlist) =
        repository.update(playlist.copy(name = playlist.name.trim()))
}
