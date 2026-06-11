package com.kovhan.feature.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.EntryProviderScope
import com.kovhan.core.navigation.BottomSheetEntryBuilder
import com.kovhan.core.navigation.BottomSheetKey
import com.kovhan.core.navigation.ChangePhotoSheetKey
import com.kovhan.core.navigation.EditFieldSheetKey
import com.kovhan.core.navigation.LanguageSheetKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.PhotoAction
import com.kovhan.core.navigation.ReminderTimeSheetKey
import com.kovhan.core.navigation.ThemeSheetKey
import com.kovhan.feature.main.presentation.edit_profile.component.ChangePhotoBottomSheet
import com.kovhan.feature.main.presentation.edit_profile.component.FieldBottomSheet
import com.kovhan.feature.main.presentation.profile.component.LanguageBottomSheet
import com.kovhan.feature.main.presentation.profile.component.ReminderTimePickerSheet
import com.kovhan.feature.main.presentation.profile.component.ThemeBottomSheet
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainBottomSheetEntryBuilder @Inject constructor() : BottomSheetEntryBuilder {

    override fun build(
        scope: EntryProviderScope<BottomSheetKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<ThemeSheetKey> { key ->
            val coroutineScope = rememberCoroutineScope()
            ThemeBottomSheet(
                selected = key.selected,
                onThemeSelected = { theme ->
                    coroutineScope.launch {
                        coordinator.emitResult(NavigationCoordinator.KEY_SELECTED_THEME, theme)
                        coordinator.dismissBottomSheet()
                    }
                },
                onDismiss = coordinator::dismissBottomSheet,
            )
        }

        scope.entry<LanguageSheetKey> { key ->
            val coroutineScope = rememberCoroutineScope()
            LanguageBottomSheet(
                selected = key.selected,
                onLanguageSelected = { language ->
                    coroutineScope.launch {
                        coordinator.emitResult(NavigationCoordinator.KEY_SELECTED_LANGUAGE, language)
                        coordinator.dismissBottomSheet()
                    }
                },
                onDismiss = coordinator::dismissBottomSheet,
            )
        }

        scope.entry<ReminderTimeSheetKey> { key ->
            val coroutineScope = rememberCoroutineScope()
            ReminderTimePickerSheet(
                initialHour = key.hour,
                initialMinute = key.minute,
                onConfirm = { hour, minute ->
                    coroutineScope.launch {
                        coordinator.emitResult(NavigationCoordinator.KEY_REMINDER_TIME, hour to minute)
                        coordinator.dismissBottomSheet()
                    }
                },
                onDismiss = coordinator::dismissBottomSheet,
            )
        }

        scope.entry<ChangePhotoSheetKey> {
            val coroutineScope = rememberCoroutineScope()
            ChangePhotoBottomSheet(
                onTakePhoto = {
                    coroutineScope.launch {
                        coordinator.emitResult(NavigationCoordinator.KEY_PHOTO_ACTION, PhotoAction.TAKE)
                        coordinator.dismissBottomSheet()
                    }
                },
                onPickGallery = {
                    coroutineScope.launch {
                        coordinator.emitResult(NavigationCoordinator.KEY_PHOTO_ACTION, PhotoAction.GALLERY)
                        coordinator.dismissBottomSheet()
                    }
                },
                onRemovePhoto = {
                    coroutineScope.launch {
                        coordinator.emitResult(NavigationCoordinator.KEY_PHOTO_ACTION, PhotoAction.REMOVE)
                        coordinator.dismissBottomSheet()
                    }
                },
                onDismiss = coordinator::dismissBottomSheet,
            )
        }

        scope.entry<EditFieldSheetKey> { key ->
            val coroutineScope = rememberCoroutineScope()
            FieldBottomSheet(
                field = key.field,
                initialValue = key.initialValue,
                onSave = { value ->
                    coroutineScope.launch {
                        coordinator.emitResult(NavigationCoordinator.KEY_EDIT_FIELD_VALUE, key.field to value)
                        coordinator.dismissBottomSheet()
                    }
                },
                onDismiss = coordinator::dismissBottomSheet,
            )
        }
    }
}
