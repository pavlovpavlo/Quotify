package com.kovhan.feature.entitydetails.presentation.collection_style.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.kovhan.core.navigation.EditCollectionStyleSheetKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.feature.entitydetails.presentation.collection_style.EditCollectionStyleSheet
import com.kovhan.feature.entitydetails.presentation.collection_style.model.CollectionStyleDraft
import com.kovhan.feature.entitydetails.navigation.EntityCollectionStyleResult
import com.kovhan.feature.entitydetails.navigation.KEY_ENTITY_STYLE_RESULT
import kotlinx.coroutines.launch

@Composable
internal fun EditCollectionStyleSheetEntry(
    key: EditCollectionStyleSheetKey,
    coordinator: NavigationCoordinator,
) {
    val scope = rememberCoroutineScope()
    var draft by remember(key) {
        mutableStateOf(
            CollectionStyleDraft(
                iconId = key.iconId,
                tone = key.tone,
            ),
        )
    }

    EditCollectionStyleSheet(
        draft = draft,
        onToneChange = { draft = draft.copy(tone = it) },
        onIconChange = { draft = draft.copy(iconId = it) },
        onSave = {
            scope.launch {
                coordinator.dismissBottomSheetWithResult(
                    KEY_ENTITY_STYLE_RESULT,
                    EntityCollectionStyleResult(
                        iconId = draft.iconId,
                        tone = draft.tone,
                    ),
                )
            }
        },
        onDismiss = coordinator::dismissBottomSheet,
    )
}
