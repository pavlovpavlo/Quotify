package com.kovhan.feature.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import com.kovhan.core.navigation.BottomSheetEntryBuilder
import com.kovhan.core.navigation.BottomSheetKey
import com.kovhan.core.navigation.ChangePhotoSheetKey
import com.kovhan.core.navigation.EditFieldSheetKey
import com.kovhan.core.navigation.LanguageSheetKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.ReminderTimeSheetKey
import com.kovhan.core.navigation.ThemeSheetKey
import com.kovhan.feature.main.presentation.change_photo.navigation.ChangePhotoSheetEntry
import com.kovhan.feature.main.presentation.edit_field.navigation.EditFieldSheetEntry
import com.kovhan.feature.main.presentation.language.navigation.LanguageSheetEntry
import com.kovhan.feature.main.presentation.reminder_time.navigation.ReminderTimeSheetEntry
import com.kovhan.feature.main.presentation.theme.navigation.ThemeSheetEntry
import javax.inject.Inject

class MainBottomSheetEntryBuilder @Inject constructor() : BottomSheetEntryBuilder {

    override fun build(
        scope: EntryProviderScope<BottomSheetKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<ThemeSheetKey> { key ->
            ThemeSheetEntry(
                key = key,
                coordinator = coordinator,
            )
        }

        scope.entry<LanguageSheetKey> { key ->
            LanguageSheetEntry(
                key = key,
                coordinator = coordinator,
            )
        }

        scope.entry<ReminderTimeSheetKey> { key ->
            ReminderTimeSheetEntry(
                key = key,
                coordinator = coordinator,
            )
        }

        scope.entry<ChangePhotoSheetKey> {
            ChangePhotoSheetEntry(coordinator = coordinator)
        }

        scope.entry<EditFieldSheetKey> { key ->
            EditFieldSheetEntry(
                key = key,
                coordinator = coordinator,
            )
        }
    }
}
