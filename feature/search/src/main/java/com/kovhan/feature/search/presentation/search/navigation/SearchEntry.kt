package com.kovhan.feature.search.presentation.search.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.ConfirmDialogKey
import com.kovhan.core.navigation.EditQuoteKey
import com.kovhan.core.navigation.EntityDetailsKey
import com.kovhan.core.navigation.EntityType
import com.kovhan.core.navigation.MoveQuoteResult
import com.kovhan.core.navigation.MoveQuoteSheetKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.QuoteEditDraft
import com.kovhan.core.navigation.SearchKey
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.search.presentation.search.SearchScreen
import com.kovhan.feature.search.presentation.search.SearchViewModel
import com.kovhan.feature.search.presentation.search.mvi.SearchEffect

@Composable
internal fun SearchEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<SearchViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val generalName = stringResource(DsR.string.collection_general)

    LaunchedEffect(generalName) {
        viewModel.bind(generalName)
    }

    var editResultDrained by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!editResultDrained) {
            coordinator.clearResult(NavigationCoordinator.KEY_QUOTE_EDIT_RESULT)
            editResultDrained = true
        }
        coordinator.observeResult<QuoteEditDraft>(NavigationCoordinator.KEY_QUOTE_EDIT_RESULT)
            .collect { draft -> draft?.let(viewModel::onEditQuoteSaved) }
    }

    LaunchedEffect(Unit) {
        coordinator.clearResult(NavigationCoordinator.KEY_MOVE_QUOTE_RESULT)
        coordinator.observeResult<MoveQuoteResult>(NavigationCoordinator.KEY_MOVE_QUOTE_RESULT)
            .collect { result ->
                result ?: return@collect
                viewModel.onMoveQuoteConfirmed(result.quoteId, result.targetCollectionId)
            }
    }

    LaunchedEffect(Unit) {
        coordinator.clearResult(NavigationCoordinator.KEY_SEARCH_QUOTE_DELETE)
        coordinator.observeResult<String>(NavigationCoordinator.KEY_SEARCH_QUOTE_DELETE)
            .collect { quoteId ->
                quoteId ?: return@collect
                viewModel.onDeleteQuoteConfirmed(quoteId)
            }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is SearchEffect.OpenQuoteEditor -> coordinator.navigate(
                    EditQuoteKey(
                        quoteId = effect.draft.quoteId,
                        text = effect.draft.text,
                        authorName = effect.draft.authorName,
                        bookName = effect.draft.bookName,
                        tags = effect.draft.tags,
                        aiTags = effect.draft.aiTags,
                        inWidgetPlaylist = effect.draft.inWidgetPlaylist,
                        inPushPlaylist = effect.draft.inPushPlaylist,
                        page = effect.draft.page,
                        authorOptions = effect.authorOptions,
                        bookOptions = effect.bookOptions,
                        tagPool = effect.tagPool,
                    ),
                )

                is SearchEffect.OpenMoveQuoteSheet -> coordinator.showBottomSheet(
                    MoveQuoteSheetKey(
                        quoteId = effect.quoteId,
                        selectedCollectionId = null,
                        excludedCollectionId = null,
                        keepsFavourite = effect.keepsFavourite,
                    ),
                )

                is SearchEffect.OpenDeleteQuoteDialog -> coordinator.showDialog(
                    ConfirmDialogKey(
                        iconRes = DsR.drawable.ic_trash,
                        titleRes = DsR.string.collection_details_delete_quote_title,
                        messageRes = DsR.string.entity_delete_quote_message,
                        confirmRes = DsR.string.collection_details_delete_quote_confirm,
                        cancelRes = DsR.string.dialog_cancel,
                        resultKey = NavigationCoordinator.KEY_SEARCH_QUOTE_DELETE,
                        payload = effect.quoteId,
                    ),
                )
            }
        }
    }

    SearchScreen(
        state = state.value,
        action = viewModel,
        navAction = object : SearchScreenNavAction {
            override fun onClose() {
                coordinator.goBack()
            }

            override fun openFolder(collectionId: String) {
                coordinator.navigate(
                    key = EntityDetailsKey(
                        type = EntityType.COLLECTION,
                        entityId = collectionId,
                    ),
                    popUpTo = SearchKey,
                )
            }

            override fun openEntity(type: EntityType, entityId: String, title: String) {
                coordinator.navigate(EntityDetailsKey(type, entityId, title))
            }
        },
        paddingValues = paddingValues,
    )
}
