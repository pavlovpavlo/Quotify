package com.kovhan.domain.widget.use_case.playlist

import com.kovhan.core.models.widget.WidgetSource
import com.kovhan.domain.widget.PlaylistRepository
import com.kovhan.domain.widget.WidgetSettingsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeletePlaylistUseCase @Inject constructor(
    private val repository: PlaylistRepository,
    private val widgetSettingsRepository: WidgetSettingsRepository,
) {
    suspend operator fun invoke(id: String) {
        repository.delete(id)
        // If the deleted playlist was the widget's source, fall back to "all"
        // so the widget keeps showing quotes instead of resolving to nothing.
        if (widgetSettingsRepository.observe().first().source == WidgetSource.Playlist(id)) {
            widgetSettingsRepository.setSource(WidgetSource.All)
        }
    }
}
