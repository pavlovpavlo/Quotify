package com.kovhan.feature.entitydetails.presentation.quote_edit

import androidx.compose.foundation.background
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.kovhan.core.ui.component.combobox.QuotifyComboboxField
import com.kovhan.core.ui.component.playlist.QuotifyPlaylistOptions
import com.kovhan.core.ui.component.quote.EditableTagChips
import com.kovhan.core.ui.component.quote.QuotePageInputField
import com.kovhan.core.ui.component.text.QuotifyFieldLabel
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.entitydetails.presentation.entity_details.model.EntityQuoteDraft
import com.kovhan.feature.entitydetails.presentation.quote_edit.component.QuoteEditFooter
import com.kovhan.feature.entitydetails.presentation.quote_edit.component.QuoteEditTextField
import com.kovhan.feature.entitydetails.presentation.quote_edit.component.QuoteEditTopBar
import com.kovhan.design.systems.R as DsR

@Composable
internal fun EntityQuoteEditScreen(
    draft: EntityQuoteDraft,
    authorOptions: List<String>,
    bookOptions: List<String>,
    onDraftChange: (EntityQuoteDraft) -> Unit,
    onOpenTagSheet: () -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgPrimary),
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
            QuoteEditTopBar(onBack = onBack)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = dimensions.space5)
                    .padding(top = dimensions.space2, bottom = dimensions.space6),
                verticalArrangement = Arrangement.spacedBy(dimensions.space4),
            ) {
                QuoteEditTextField(
                    value = draft.text,
                    onValueChange = { onDraftChange(draft.copy(text = it)) },
                )

                QuotifyComboboxField(
                    label = stringResource(DsR.string.details_field_author),
                    value = draft.authorName,
                    onValueChange = { onDraftChange(draft.copy(authorName = it)) },
                    onOptionSelected = { onDraftChange(draft.copy(authorName = it)) },
                    options = authorOptions.matching(draft.authorName),
                    placeholder = stringResource(DsR.string.details_author_ph),
                    imeAction = ImeAction.Next,
                )

                QuotifyComboboxField(
                    label = stringResource(DsR.string.details_field_book),
                    value = draft.bookName,
                    onValueChange = { onDraftChange(draft.copy(bookName = it)) },
                    onOptionSelected = { onDraftChange(draft.copy(bookName = it)) },
                    options = bookOptions.matching(draft.bookName),
                    placeholder = stringResource(DsR.string.details_book_ph),
                    imeAction = ImeAction.Next,
                )

                QuotePageInputField(
                    value = draft.page,
                    onValueChange = { onDraftChange(draft.copy(page = it)) },
                    label = stringResource(DsR.string.details_field_page),
                    imeAction = ImeAction.Done,
                    onImeAction = { focusManager.clearFocus() },
                )

                Column(modifier = Modifier.fillMaxWidth()) {
                    QuotifyFieldLabel(text = stringResource(DsR.string.details_field_tags))
                    EditableTagChips(
                        tags = draft.tags,
                        onRemoveTag = { tag ->
                            onDraftChange(draft.copy(tags = draft.tags.filterNot { it == tag }))
                        },
                        onAddTag = {
                            focusManager.clearFocus()
                            onOpenTagSheet()
                        },
                    )
                }

                QuotifyPlaylistOptions(
                    widgetEnabled = draft.inWidgetPlaylist,
                    pushEnabled = draft.inPushPlaylist,
                    onWidgetToggle = { onDraftChange(draft.copy(inWidgetPlaylist = it)) },
                    onPushToggle = { onDraftChange(draft.copy(inPushPlaylist = it)) },
                )
            }

            QuoteEditFooter(
                onSave = onSave,
                enabled = draft.text.isNotBlank(),
            )
        }
    }
}

private fun List<String>.matching(query: String): List<String> {
    val trimmed = query.trim()
    return if (trimmed.isEmpty()) this else filter { it.contains(trimmed, ignoreCase = true) }
}
