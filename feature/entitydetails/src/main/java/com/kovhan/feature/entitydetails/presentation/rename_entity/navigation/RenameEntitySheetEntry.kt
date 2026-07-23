package com.kovhan.feature.entitydetails.presentation.rename_entity.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.kovhan.core.navigation.EntityType
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.RenameEntitySheetKey
import com.kovhan.core.ui.component.bottomsheet.RenameBottomSheet
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.entitydetails.navigation.KEY_ENTITY_RENAME_RESULT
import kotlinx.coroutines.launch

@Composable
internal fun RenameEntitySheetEntry(
    key: RenameEntitySheetKey,
    coordinator: NavigationCoordinator,
) {
    val scope = rememberCoroutineScope()
    val isCollection = key.type == EntityType.COLLECTION

    RenameBottomSheet(
        title = stringResource(
            if (isCollection) {
                DsR.string.collection_details_rename_title
            } else {
                DsR.string.entity_rename_title
            },
        ),
        label = stringResource(DsR.string.collection_details_name_label),
        placeholder = stringResource(
            if (isCollection) {
                DsR.string.collection_details_name_placeholder
            } else {
                DsR.string.entity_name_placeholder
            },
        ),
        initialName = key.initialName,
        confirmText = stringResource(DsR.string.details_save),
        maxLength = if (isCollection) 40 else 60,
        onSave = { name ->
            scope.launch {
                coordinator.dismissBottomSheetWithResult(KEY_ENTITY_RENAME_RESULT, name)
            }
        },
        onDismiss = coordinator::dismissBottomSheet,
    )
}
