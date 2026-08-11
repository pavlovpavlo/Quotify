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
import androidx.compose.ui.res.stringResource
import com.kovhan.core.ui.component.combobox.QuotifyComboboxField
import com.kovhan.core.ui.component.playlist.QuotifyPlaylistOptions
import com.kovhan.core.ui.component.quote.QuotePageInputField
import com.kovhan.core.ui.component.quote.QuoteTextInputField
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.feature.addquote.presentation.details.component.DetailsFooter
import com.kovhan.feature.addquote.presentation.details.component.DetailsTopBar
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
                QuoteTextInputField(
                    value = state.quote,
                    onValueChange = intent::onQuoteChanged,
                    label = stringResource(R.string.details_field_quote),
                    placeholder = stringResource(R.string.details_quote_ph),
                    minHeight = dimensions.size96,
                )
                QuotifyComboboxField(
                    label = stringResource(R.string.details_field_author),
                    value = state.authorQuery,
                    onValueChange = intent::onAuthorQueryChanged,
                    onOptionSelected = intent::onAuthorPicked,
                    options = state.filteredAuthorNames,
                    placeholder = stringResource(R.string.details_author_ph),
                )
                QuotifyComboboxField(
                    label = stringResource(R.string.details_field_book),
                    value = state.bookQuery,
                    onValueChange = intent::onBookQueryChanged,
                    onOptionSelected = intent::onBookPicked,
                    options = state.filteredBookNames,
                    placeholder = stringResource(R.string.details_book_ph),
                )
                QuotePageInputField(
                    value = state.page,
                    onValueChange = intent::onPageChanged,
                    label = stringResource(R.string.details_field_page),
                )
                TagEditor(
                    tags = state.selectedTags,
                    onRemoveTag = intent::onRemoveTag,
                    onAddTag = intent::onOpenTagSheet,
                )
                QuotifyPlaylistOptions(
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
