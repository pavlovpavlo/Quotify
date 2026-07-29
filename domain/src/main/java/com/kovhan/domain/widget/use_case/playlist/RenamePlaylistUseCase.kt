package com.kovhan.domain.widget.use_case.playlist

import com.kovhan.domain.widget.PlaylistRepository
import javax.inject.Inject

class RenamePlaylistUseCase @Inject constructor(
    private val repository: PlaylistRepository,
) {
    suspend operator fun invoke(id: String, name: String) = repository.rename(id, name.trim())
}
