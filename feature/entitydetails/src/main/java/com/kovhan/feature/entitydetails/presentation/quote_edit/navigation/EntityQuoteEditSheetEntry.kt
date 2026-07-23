package com.kovhan.feature.entitydetails.presentation.quote_edit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.EditQuoteSheetKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.TagSheetKey
import com.kovhan.core.navigation.TagSheetResult
import com.kovhan.feature.entitydetails.presentation.quote_edit.EntityQuoteEditSheet
import com.kovhan.feature.entitydetails.navigation.KEY_ENTITY_QUOTE_EDIT_RESULT
import com.kovhan.feature.entitydetails.presentation.quote_edit.EntityQuoteEditSheetViewModel
import com.kovhan.feature.entitydetails.presentation.quote_edit.mvi.EntityQuoteEditSheetEffect

@Composable
internal fun EntityQuoteEditSheetEntry(
    key: EditQuoteSheetKey,
    coordinator: NavigationCoordinator,
) {
    val viewModel = hiltViewModel<EntityQuoteEditSheetViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key) {
        viewModel.bind(key)
    }

    LaunchedEffect(Unit) {
        coordinator.observeResult<TagSheetResult>(NavigationCoordinator.KEY_TAG_SHEET_RESULT)
            .collect { result ->
                result ?: return@collect
                viewModel.onTagSheetApplied(result.selectedTags, result.aiTags)
            }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is EntityQuoteEditSheetEffect.OpenTagSheet -> {
                    coordinator.showBottomSheet(
                        TagSheetKey(
                            quoteText = effect.quoteText,
                            selectedTags = effect.selectedTags,
                            tagPool = effect.tagPool,
                            aiTags = effect.aiTags,
                        ),
                    )
                }

                is EntityQuoteEditSheetEffect.CloseWithResult -> {
                    coordinator.dismissBottomSheetWithResult(
                        KEY_ENTITY_QUOTE_EDIT_RESULT,
                        effect.draft,
                    )
                }
            }
        }
    }

    EntityQuoteEditSheet(
        draft = state.value.draft,
        authorOptions = state.value.authorOptions,
        bookOptions = state.value.bookOptions,
        onDraftChange = viewModel::onDraftChanged,
        onOpenTagSheet = viewModel::onOpenTagSheetRequested,
        onSave = viewModel::onSaveRequested,
        onDismiss = coordinator::dismissBottomSheet,
    )
}
