package com.kovhan.feature.addquote.presentation.details.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.AddQuoteKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.QuoteDetailsKey
import com.kovhan.core.navigation.SaveQuoteCollectionSheetKey
import com.kovhan.core.navigation.TagSheetKey
import com.kovhan.core.navigation.TagSheetResult
import com.kovhan.feature.addquote.presentation.details.DetailsScreen
import com.kovhan.feature.addquote.presentation.details.DetailsViewModel
import com.kovhan.feature.addquote.presentation.details.mvi.DetailsEffect
import com.kovhan.feature.addquote.presentation.details.mvi.QuoteDraft

@Composable
internal fun DetailsEntry(
    key: QuoteDetailsKey,
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<DetailsViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    val navAction = object : DetailsScreenNavAction {
        override fun back() {
            coordinator.goBack()
        }

        override fun close() {
            coordinator.popBackTo(AddQuoteKey::class, inclusive = true)
        }

        override fun openTagSheet() {
            coordinator.showBottomSheet(
                TagSheetKey(
                    quoteText = state.value.quote.text,
                    selectedTags = state.value.selectedTags,
                    tagPool = state.value.tagPool,
                    aiTags = state.value.aiTags,
                ),
            )
        }

        override fun proceedToSave(draft: QuoteDraft) {
            coordinator.showBottomSheet(
                SaveQuoteCollectionSheetKey(
                    text = draft.text,
                    authorName = draft.authorName,
                    bookName = draft.bookName,
                    tagNames = draft.tagNames,
                    inWidgetPlaylist = draft.inWidgetPlaylist,
                    inPushPlaylist = draft.inPushPlaylist,
                    page = draft.page,
                ),
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onInitialQuote(key.quote)
    }

    LaunchedEffect(Unit) {
        coordinator.observeResult<TagSheetResult>(NavigationCoordinator.KEY_TAG_SHEET_RESULT).collect { result ->
            result ?: return@collect
            viewModel.onTagSheetApplied(
                selectedTags = result.selectedTags,
                aiTags = result.aiTags,
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                DetailsEffect.Back -> navAction.back()
                DetailsEffect.Close -> navAction.close()
                DetailsEffect.OpenTagSheet -> navAction.openTagSheet()
                is DetailsEffect.ProceedToSave -> navAction.proceedToSave(effect.draft)
            }
        }
    }

    DetailsScreen(
        state = state.value,
        intent = viewModel,
        paddingValues = paddingValues,
    )
}
