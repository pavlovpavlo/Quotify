package com.kovhan.feature.addquote.presentation.save_collection.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.models.quote.AddQuoteAction
import com.kovhan.core.navigation.AddQuoteKey
import com.kovhan.core.navigation.FeedbackDialogKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.NewCollectionSheetKey
import com.kovhan.core.navigation.PaywallKey
import com.kovhan.core.navigation.SaveQuoteCollectionSheetKey
import com.kovhan.core.navigation.WidgetPromoKey
import com.kovhan.feature.addquote.presentation.details.mvi.QuoteDraft
import com.kovhan.feature.addquote.presentation.save_collection.SaveQuoteCollectionSheet
import com.kovhan.feature.addquote.presentation.save_collection.SaveQuoteCollectionViewModel
import com.kovhan.feature.addquote.presentation.save_collection.mvi.SaveQuoteCollectionEffect

@Composable
internal fun SaveQuoteCollectionSheetEntry(
    key: SaveQuoteCollectionSheetKey,
    coordinator: NavigationCoordinator,
) {
    val viewModel = hiltViewModel<SaveQuoteCollectionViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val generalName = stringResource(com.kovhan.design.systems.R.string.collection_general)

    val draft = QuoteDraft(
        text = key.text,
        authorName = key.authorName,
        bookName = key.bookName,
        tagNames = key.tagNames,
        inWidgetPlaylist = key.inWidgetPlaylist,
        inPushPlaylist = key.inPushPlaylist,
        page = key.page,
    )

    LaunchedEffect(Unit) {
        viewModel.loadCollections(generalName)
    }

    var resultDrained by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!resultDrained) {
            coordinator.clearResult(NavigationCoordinator.KEY_COLLECTION_CREATED)
            resultDrained = true
        }
        coordinator.observeResult<String>(NavigationCoordinator.KEY_COLLECTION_CREATED)
            .collect { createdId ->
                if (createdId != null) {
                    viewModel.loadCollections(generalName, selectId = createdId)
                    viewModel.saveToCollection(
                        draft,
                        collectionId = createdId,
                        generalName = generalName
                    )
                }
            }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is SaveQuoteCollectionEffect.Saved -> {
                    coordinator.clearBottomSheets()
                    coordinator.popBackTo(
                        AddQuoteKey::class,
                        inclusive = true,
                        restoreOverlays = false
                    )
                    if (effect.action == AddQuoteAction.Feedback)
                        coordinator.showDialog(FeedbackDialogKey())
                    if (effect.action == AddQuoteAction.Widget)
                        coordinator.showDialog(WidgetPromoKey)
                }

                SaveQuoteCollectionEffect.ShowPaywall -> {
                    coordinator.dismissBottomSheet()
                    coordinator.navigate(PaywallKey)
                }
            }
        }
    }

    SaveQuoteCollectionSheet(
        collections = state.value.collections,
        chosenCollectionId = state.value.chosenCollectionId,
        savingCollectionId = state.value.savingCollectionId,
        onChooseCollection = viewModel::onChooseCollection,
        onSaveWidgetPlaced = viewModel::onSaveWidgetPlaced,
        onSaveToCollection = { collectionId ->
            viewModel.saveToCollection(
                draft,
                collectionId = collectionId,
                generalName = generalName,
            )
        },
        onCreateCollection = {
            coordinator.showBottomSheet(
                NewCollectionSheetKey(
                    text = key.text,
                    authorName = key.authorName,
                    bookName = key.bookName,
                    tagNames = key.tagNames,
                    inWidgetPlaylist = key.inWidgetPlaylist,
                    inPushPlaylist = key.inPushPlaylist,
                    page = key.page,
                ),
            )
        },
        onDismiss = coordinator::dismissBottomSheet,
    )
}
