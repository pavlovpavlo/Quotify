package com.kovhan.feature.addquote.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.addquote.presentation.details.component.AuthorField
import com.kovhan.feature.addquote.presentation.details.component.BookField
import com.kovhan.feature.addquote.presentation.details.component.DetailsFooter
import com.kovhan.feature.addquote.presentation.details.component.DetailsQuoteField
import com.kovhan.feature.addquote.presentation.details.component.DetailsTopBar
import com.kovhan.feature.addquote.presentation.details.component.PlaylistOptions
import com.kovhan.feature.addquote.presentation.details.component.tageditor.TagEditor
import com.kovhan.feature.addquote.presentation.details.mvi.DetailsIntent
import com.kovhan.feature.addquote.presentation.details.mvi.DetailsState

@Composable
fun DetailsScreen(
    state: DetailsState,
    intent: DetailsIntent,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgPrimary)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                )
                .imePadding(),
        ) {
            DetailsTopBar(
                onBack = intent::onBackClicked,
                onClose = intent::onCloseClicked,
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = dimensions.space5)
                    .padding(top = dimensions.space2, bottom = dimensions.space6),
                verticalArrangement = Arrangement.spacedBy(dimensions.space4),
            ) {
                DetailsQuoteField(
                    value = state.quote,
                    onValueChange = intent::onQuoteChanged,
                )
                AuthorField(
                    query = state.authorQuery,
                    onQueryChange = intent::onAuthorQueryChanged,
                    onPicked = intent::onAuthorPicked,
                    options = state.filteredAuthorNames,
                )
                BookField(
                    query = state.bookQuery,
                    onQueryChange = intent::onBookQueryChanged,
                    onPicked = intent::onBookPicked,
                    options = state.filteredBookNames,
                )
                TagEditor(
                    tags = state.selectedTags,
                    onRemoveTag = intent::onRemoveTag,
                    onAddTag = intent::onOpenTagSheet,
                )
                PlaylistOptions(
                    widgetEnabled = state.inWidgetPlaylist,
                    pushEnabled = state.inPushPlaylist,
                    onWidgetToggle = intent::onWidgetToggle,
                    onPushToggle = intent::onPushToggle,
                )
            }

            DetailsFooter(
                onSave = intent::onSaveClicked,
                enabled = state.canSave,
            )
        }

    }
}
