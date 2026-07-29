package com.kovhan.feature.widget.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.kovhan.core.navigation.EntryBuilder
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.PlaylistPickerKey
import com.kovhan.core.navigation.WidgetQuoteKey
import com.kovhan.core.navigation.WidgetSettingsKey
import com.kovhan.feature.widget.presentation.picker.navigation.PlaylistPickerEntry
import com.kovhan.feature.widget.presentation.quote.navigation.WidgetQuoteEntry
import com.kovhan.feature.widget.presentation.settings.navigation.WidgetSettingsEntry
import javax.inject.Inject

class WidgetEntryBuilder @Inject constructor() : EntryBuilder {

    override fun build(
        scope: EntryProviderScope<NavKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<WidgetSettingsKey> {
            WidgetSettingsEntry(
                coordinator = coordinator,
                paddingValues = paddingValues,
            )
        }

        scope.entry<PlaylistPickerKey> { key ->
            PlaylistPickerEntry(
                key = key,
                coordinator = coordinator,
                paddingValues = paddingValues,
            )
        }

        scope.entry<WidgetQuoteKey> { key ->
            WidgetQuoteEntry(
                key = key,
                coordinator = coordinator,
                paddingValues = paddingValues,
            )
        }
    }
}
