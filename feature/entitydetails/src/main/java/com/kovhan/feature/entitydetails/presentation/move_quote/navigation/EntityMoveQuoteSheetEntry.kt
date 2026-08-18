package com.kovhan.feature.entitydetails.presentation.move_quote.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.MoveQuoteSheetKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.NewCollectionSheetKey
import com.kovhan.design.systems.R
import com.kovhan.feature.entitydetails.presentation.move_quote.EntityMoveQuoteSheet
import com.kovhan.feature.entitydetails.presentation.move_quote.EntityMoveQuoteSheetViewModel
import com.kovhan.feature.entitydetails.presentation.move_quote.mvi.EntityMoveQuoteSheetEffect
import com.kovhan.core.navigation.MoveQuoteResult
import com.kovhan.feature.entitydetails.navigation.KEY_ENTITY_MOVE_QUOTE_RESULT

@Composable
internal fun EntityMoveQuoteSheetEntry(
    key: MoveQuoteSheetKey,
    coordinator: NavigationCoordinator,
) {
    val viewModel = hiltViewModel<EntityMoveQuoteSheetViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val generalName = stringResource(R.string.collection_general)

    LaunchedEffect(key, generalName) {
        viewModel.bind(key, generalName)
    }

    LaunchedEffect(Unit) {
        coordinator.clearResult(NavigationCoordinator.KEY_COLLECTION_CREATED)
        coordinator.observeResult<String>(NavigationCoordinator.KEY_COLLECTION_CREATED)
            .collect { createdId ->
                createdId ?: return@collect
                viewModel.onCollectionCreated(createdId)
            }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                EntityMoveQuoteSheetEffect.OpenNewCollectionSheet -> {
                    coordinator.showBottomSheet(
                        NewCollectionSheetKey(
                            text = "",
                            authorName = null,
                            bookName = null,
                            tagNames = emptyList(),
                            inWidgetPlaylist = false,
                            inPushPlaylist = false,
                        ),
                    )
                }

                is EntityMoveQuoteSheetEffect.CloseWithResult -> {
                    coordinator.dismissBottomSheetWithResult(
                        KEY_ENTITY_MOVE_QUOTE_RESULT,
                        MoveQuoteResult(
                            quoteId = effect.quoteId,
                            targetCollectionId = effect.targetCollectionId,
                        ),
                    )
                }
            }
        }
    }

    EntityMoveQuoteSheet(
        targets = state.value.targets,
        selectedCollectionId = state.value.selectedCollectionId,
        keepsFavourite = state.value.keepsFavourite,
        onSelectCollection = viewModel::onSelectCollection,
        onAddToCollection = viewModel::onMoveRequested,
        onCreateCollection = viewModel::onCreateCollectionRequested,
        onDismiss = coordinator::dismissBottomSheet,
    )
}
