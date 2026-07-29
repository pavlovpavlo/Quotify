package com.kovhan.feature.widget.presentation.settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kovhan.core.models.widget.WidgetSource
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.widget.presentation.settings.mvi.WidgetSettingsIntent
import com.kovhan.feature.widget.presentation.settings.mvi.WidgetSettingsState

@Composable
internal fun WidgetSourceSection(
    state: WidgetSettingsState,
    intent: WidgetSettingsIntent,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        WidgetSectionLabel(stringResource(DsR.string.widget_sources_label))

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            WidgetSourceCard(
                title = stringResource(DsR.string.widget_source_all),
                count = state.allCount,
                iconRes = DsR.drawable.ic_grid,
                selected = state.selectedSource is WidgetSource.All,
                showChevron = false,
                onRowClick = { intent.onSourceSelected(WidgetSource.All) },
                onRadioClick = { intent.onSourceSelected(WidgetSource.All) },
            )

            WidgetSourceCard(
                title = stringResource(DsR.string.widget_source_favorites),
                count = state.favouritesCount,
                iconRes = DsR.drawable.ic_heart_filled,
                selected = state.selectedSource is WidgetSource.Favorites,
                showChevron = false,
                onRowClick = { intent.onSourceSelected(WidgetSource.Favorites) },
                onRadioClick = { intent.onSourceSelected(WidgetSource.Favorites) },
            )

            state.playlists.forEach { entry ->
                val source = WidgetSource.Playlist(entry.playlist.id)
                WidgetSourceCard(
                    title = entry.playlist.name,
                    count = entry.quoteCount,
                    iconRes = DsR.drawable.ic_folder,
                    selected = state.selectedSource == source,
                    showChevron = true,
                    onRowClick = { intent.onEditPlaylistClicked(entry.playlist.id) },
                    onRadioClick = { intent.onSourceSelected(source) },
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        CreatePlaylistButton(
            onClick = intent::onCreatePlaylistClicked,
            modifier = Modifier.padding(top = 2.dp),
        )
    }
}
