package com.kovhan.feature.entitydetails.presentation.collection_style.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.EditCollectionStyleSheetKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.PaywallKey
import com.kovhan.core.navigation.models.PaywallOrigin
import com.kovhan.feature.entitydetails.presentation.collection_style.EditCollectionStyleSheet
import com.kovhan.feature.entitydetails.presentation.collection_style.EditCollectionStyleViewModel
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
    val viewModel = hiltViewModel<EditCollectionStyleViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
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
        locked = !state.isPremium,
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
        // Пейвол відкриваємо поверх — шит лишається в збережених оверлеях
        // власника екрана, тож після покупки повернення відкриє його вже без замка.
        onUnlock = {
            viewModel.onUnlockClicked()
            coordinator.navigate(PaywallKey(PaywallOrigin.LIMIT))
        },
        onDismiss = coordinator::dismissBottomSheet,
    )
}
