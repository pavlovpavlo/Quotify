package com.kovhan.feature.widget.presentation.quote.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.glance.appwidget.updateAll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.ConfirmDialogKey
import com.kovhan.core.navigation.EditQuoteSheetKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.QuoteEditDraft
import com.kovhan.core.navigation.WidgetQuoteKey
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.widget.glance.QuotifyGlanceWidget
import com.kovhan.feature.widget.presentation.quote.WidgetQuoteScreen
import com.kovhan.feature.widget.presentation.quote.WidgetQuoteViewModel
import com.kovhan.feature.widget.presentation.quote.mvi.WidgetQuoteEffect

@Composable
internal fun WidgetQuoteEntry(
    key: WidgetQuoteKey,
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<WidgetQuoteViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val generalName = stringResource(DsR.string.collection_general)

    LaunchedEffect(key.quoteId, generalName) {
        viewModel.bind(key.quoteId, generalName)
    }

    LaunchedEffect(Unit) {
        coordinator.clearResult(NavigationCoordinator.KEY_QUOTE_EDIT_RESULT)
        coordinator.observeResult<QuoteEditDraft>(NavigationCoordinator.KEY_QUOTE_EDIT_RESULT)
            .collect { draft -> draft?.let(viewModel::onEditSaved) }
    }

    LaunchedEffect(Unit) {
        coordinator.clearResult(NavigationCoordinator.KEY_WIDGET_QUOTE_DELETE)
        coordinator.observeResult<Boolean>(NavigationCoordinator.KEY_WIDGET_QUOTE_DELETE)
            .collect { confirmed -> if (confirmed == true) viewModel.onDeleteConfirmed() }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is WidgetQuoteEffect.OpenEditSheet ->
                    coordinator.showBottomSheet(
                        EditQuoteSheetKey(
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

                is WidgetQuoteEffect.OpenDeleteDialog ->
                    coordinator.showDialog(
                        ConfirmDialogKey(
                            iconRes = DsR.drawable.ic_trash,
                            titleRes = DsR.string.collection_details_delete_quote_title,
                            messageRes = DsR.string.entity_delete_quote_message,
                            confirmRes = DsR.string.collection_details_delete_quote_confirm,
                            cancelRes = DsR.string.dialog_cancel,
                            resultKey = NavigationCoordinator.KEY_WIDGET_QUOTE_DELETE,
                        ),
                    )

                WidgetQuoteEffect.Edited ->
                    QuotifyGlanceWidget().updateAll(context)

                WidgetQuoteEffect.Deleted -> {
                    QuotifyGlanceWidget().updateAll(context)
                    coordinator.goBack()
                }
            }
        }
    }

    WidgetQuoteScreen(
        state = state.value,
        intent = viewModel,
        onBack = coordinator::goBack,
        paddingValues = paddingValues,
    )
}
