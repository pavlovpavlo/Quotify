package com.kovhan.feature.widget.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import com.kovhan.core.navigation.BottomSheetEntryBuilder
import com.kovhan.core.navigation.BottomSheetKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.PlaylistNameSheetKey
import com.kovhan.core.navigation.WidgetFrequencySheetKey
import com.kovhan.feature.widget.presentation.frequency.navigation.WidgetFrequencySheetEntry
import com.kovhan.feature.widget.presentation.picker.navigation.PlaylistNameSheetEntry
import javax.inject.Inject

class WidgetBottomSheetEntryBuilder @Inject constructor() : BottomSheetEntryBuilder {

    override fun build(
        scope: EntryProviderScope<BottomSheetKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<WidgetFrequencySheetKey> { key ->
            WidgetFrequencySheetEntry(
                key = key,
                coordinator = coordinator,
            )
        }

        scope.entry<PlaylistNameSheetKey> { key ->
            PlaylistNameSheetEntry(
                key = key,
                coordinator = coordinator,
            )
        }
    }
}
