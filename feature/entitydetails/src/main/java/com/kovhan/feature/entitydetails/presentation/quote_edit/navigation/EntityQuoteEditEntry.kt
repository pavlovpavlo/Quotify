package com.kovhan.feature.entitydetails.presentation.quote_edit.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.EditQuoteKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.TagSheetKey
import com.kovhan.core.navigation.TagSheetResult
import com.kovhan.feature.entitydetails.navigation.KEY_ENTITY_QUOTE_EDIT_RESULT
import com.kovhan.feature.entitydetails.presentation.quote_edit.EntityQuoteEditScreen
import com.kovhan.feature.entitydetails.presentation.quote_edit.EntityQuoteEditViewModel
import com.kovhan.feature.entitydetails.presentation.quote_edit.mvi.EntityQuoteEditEffect

@Composable
internal fun EntityQuoteEditEntry(
    key: EditQuoteKey,
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<EntityQuoteEditViewModel>()
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
                is EntityQuoteEditEffect.OpenTagSheet -> {
                    coordinator.showBottomSheet(
                        TagSheetKey(
                            quoteText = effect.quoteText,
                            selectedTags = effect.selectedTags,
                            tagPool = effect.tagPool,
                            aiTags = effect.aiTags,
                        ),
                    )
                }

                is EntityQuoteEditEffect.CloseWithResult -> {
                    coordinator.goBackWithResult(
                        KEY_ENTITY_QUOTE_EDIT_RESULT,
                        effect.draft,
                    )
                }
            }
        }
    }

    EntityQuoteEditScreen(
        draft = state.value.draft,
        authorOptions = state.value.authorOptions,
        bookOptions = state.value.bookOptions,
        onDraftChange = viewModel::onDraftChanged,
        onOpenTagSheet = viewModel::onOpenTagSheetRequested,
        onSave = viewModel::onSaveRequested,
        onBack = coordinator::goBack,
        paddingValues = paddingValues,
    )
}
