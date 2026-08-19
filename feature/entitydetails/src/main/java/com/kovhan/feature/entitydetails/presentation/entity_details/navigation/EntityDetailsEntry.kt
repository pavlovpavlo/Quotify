package com.kovhan.feature.entitydetails.presentation.entity_details.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.AddQuoteKey
import com.kovhan.core.navigation.models.AddQuoteEntryPoint
import com.kovhan.core.navigation.ConfirmDialogKey
import com.kovhan.core.navigation.EditCollectionStyleSheetKey
import com.kovhan.core.navigation.EditQuoteKey
import com.kovhan.core.navigation.EntityDetailsKey
import com.kovhan.core.navigation.EntityType
import com.kovhan.core.navigation.MoveQuoteSheetKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.QuoteRemovalMode
import com.kovhan.core.navigation.RenameEntitySheetKey
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.entitydetails.navigation.EntityCollectionStyleResult
import com.kovhan.core.navigation.MoveQuoteResult
import com.kovhan.feature.entitydetails.navigation.KEY_ENTITY_DELETE_CONFIRMED
import com.kovhan.feature.entitydetails.navigation.KEY_ENTITY_DELETE_QUOTE_RESULT
import com.kovhan.feature.entitydetails.navigation.KEY_ENTITY_MOVE_QUOTE_RESULT
import com.kovhan.feature.entitydetails.navigation.KEY_ENTITY_QUOTE_EDIT_RESULT
import com.kovhan.feature.entitydetails.navigation.KEY_ENTITY_RENAME_RESULT
import com.kovhan.feature.entitydetails.navigation.KEY_ENTITY_STYLE_RESULT
import com.kovhan.feature.entitydetails.presentation.entity_details.model.EntityQuoteDraft
import com.kovhan.feature.entitydetails.presentation.entity_details.EntityDetailsScreen
import com.kovhan.feature.entitydetails.presentation.entity_details.EntityDetailsViewModel
import com.kovhan.feature.entitydetails.presentation.entity_details.mvi.EntityDetailsEffect

@Composable
internal fun EntityDetailsEntry(
    key: EntityDetailsKey,
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<EntityDetailsViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val generalName = stringResource(DsR.string.collection_general)

    LaunchedEffect(key.type, key.entityId, key.title, generalName) {
        viewModel.bind(key.type, key.entityId, key.title, generalName)
    }

    LaunchedEffect(Unit) {
        coordinator.observeResult<String>(KEY_ENTITY_RENAME_RESULT).collect { name ->
            name ?: return@collect
            viewModel.onRenameConfirmed(name)
        }
    }

    LaunchedEffect(Unit) {
        coordinator.observeResult<EntityCollectionStyleResult>(KEY_ENTITY_STYLE_RESULT)
            .collect { result ->
                result ?: return@collect
                viewModel.onCollectionStyleConfirmed(result.iconId, result.tone)
            }
    }

    LaunchedEffect(Unit) {
        coordinator.observeResult<EntityQuoteDraft>(KEY_ENTITY_QUOTE_EDIT_RESULT)
            .collect { draft ->
                draft ?: return@collect
                viewModel.onEditQuoteSaved(draft)
            }
    }

    LaunchedEffect(Unit) {
        coordinator.observeResult<MoveQuoteResult>(KEY_ENTITY_MOVE_QUOTE_RESULT)
            .collect { result ->
                result ?: return@collect
                viewModel.onMoveQuoteConfirmed(result.quoteId, result.targetCollectionId)
            }
    }

    LaunchedEffect(Unit) {
        coordinator.observeResult<Boolean>(KEY_ENTITY_DELETE_CONFIRMED).collect { confirmed ->
            if (confirmed == true) {
                viewModel.onDeleteConfirmed()
            }
        }
    }

    LaunchedEffect(Unit) {
        coordinator.observeResult<String>(KEY_ENTITY_DELETE_QUOTE_RESULT).collect { quoteId ->
            quoteId ?: return@collect
            viewModel.onRemoveQuoteConfirmed(quoteId)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                EntityDetailsEffect.Close -> coordinator.goBack()
                is EntityDetailsEffect.OpenRenameSheet -> {
                    coordinator.showBottomSheet(
                        RenameEntitySheetKey(
                            type = effect.type,
                            initialName = effect.initialName,
                        ),
                    )
                }
                is EntityDetailsEffect.OpenCollectionStyleSheet -> {
                    coordinator.showBottomSheet(
                        EditCollectionStyleSheetKey(
                            iconId = effect.iconId,
                            tone = effect.tone,
                        ),
                    )
                }
                is EntityDetailsEffect.OpenQuoteEditor -> {
                    coordinator.navigate(
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
                }
                is EntityDetailsEffect.OpenMoveQuoteSheet -> {
                    coordinator.showBottomSheet(
                        MoveQuoteSheetKey(
                            quoteId = effect.quoteId,
                            selectedCollectionId = effect.selectedCollectionId,
                            excludedCollectionId = effect.excludedCollectionId,
                            keepsFavourite = effect.keepsFavourite,
                        ),
                    )
                }
                is EntityDetailsEffect.OpenDeleteEntityDialog -> {
                    val isCollection = effect.type == EntityType.COLLECTION
                    coordinator.showDialog(
                        ConfirmDialogKey(
                            iconRes = DsR.drawable.ic_trash,
                            titleRes = if (isCollection) {
                                DsR.string.collection_details_delete_collection_title
                            } else {
                                DsR.string.entity_delete_title
                            },
                            messageRes = if (isCollection) {
                                DsR.string.collection_details_delete_collection_message
                            } else {
                                DsR.string.entity_delete_message
                            },
                            confirmRes = if (isCollection) {
                                DsR.string.collection_details_delete_collection_confirm
                            } else {
                                DsR.string.entity_delete_confirm
                            },
                            cancelRes = DsR.string.dialog_cancel,
                            resultKey = KEY_ENTITY_DELETE_CONFIRMED,
                        ),
                    )
                }
                is EntityDetailsEffect.OpenDeleteQuoteDialog -> {
                    val messageRes = when (effect.mode) {
                        QuoteRemovalMode.DELETE ->
                            DsR.string.entity_delete_quote_message
                        QuoteRemovalMode.REMOVE_FROM_COLLECTION ->
                            DsR.string.collection_details_delete_quote_message
                        QuoteRemovalMode.REMOVE_FROM_ENTITY ->
                            DsR.string.entity_remove_quote_message
                        QuoteRemovalMode.REMOVE_FROM_FAVOURITES ->
                            DsR.string.collection_details_remove_favourite_message
                    }
                    coordinator.showDialog(
                        ConfirmDialogKey(
                            iconRes = DsR.drawable.ic_trash,
                            titleRes = DsR.string.collection_details_delete_quote_title,
                            messageRes = messageRes,
                            confirmRes = DsR.string.collection_details_delete_quote_confirm,
                            cancelRes = DsR.string.dialog_cancel,
                            resultKey = KEY_ENTITY_DELETE_QUOTE_RESULT,
                            payload = effect.quoteId,
                        ),
                    )
                }
            }
        }
    }

    val navAction = object : EntityDetailsScreenNavAction {
        override fun onBack() {
            coordinator.goBack()
        }

        override fun onAddQuote() =
            coordinator.navigate(AddQuoteKey(entryPoint = AddQuoteEntryPoint.EMPTY_COLLECTION))
    }

    EntityDetailsScreen(
        state = state.value,
        intent = viewModel,
        navAction = navAction,
        paddingValues = paddingValues,
    )
}
