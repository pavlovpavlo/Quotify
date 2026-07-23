package com.kovhan.feature.entitydetails.presentation.quote_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kovhan.core.ui.component.bottomsheet.QuotifyBottomSheet
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.combobox.QuotifyComboboxField
import com.kovhan.core.ui.component.playlist.QuotifyPlaylistOptions
import com.kovhan.core.ui.component.quote.EditableTagChips
import com.kovhan.core.ui.component.text.QuotifyFieldLabel
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.entitydetails.presentation.entity_details.model.EntityQuoteDraft
import com.kovhan.feature.entitydetails.presentation.quote_edit.component.QuoteEditTextField
import com.kovhan.design.systems.R as DsR

/**
 * Bottom sheet for editing a quote — reuses the "Additional info" fields
 * (Quote / Author / Book / Tags + playlist toggles). Shared by collection and
 * entity details.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun EntityQuoteEditSheet(
    draft: EntityQuoteDraft,
    authorOptions: List<String>,
    bookOptions: List<String>,
    onDraftChange: (EntityQuoteDraft) -> Unit,
    onOpenTagSheet: () -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
) {
    val dimensions = QuotifyMaterialTheme.dimensions

    QuotifyBottomSheet(
        onDismiss = onDismiss,
        title = stringResource(DsR.string.collection_details_quote_edit_title),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = dimensions.space5, end = dimensions.space5, bottom = dimensions.space4)
                .verticalScroll(rememberScrollState()),
        ) {
            QuoteEditTextField(
                value = draft.text,
                onValueChange = { onDraftChange(draft.copy(text = it)) },
            )

            Spacer(modifier = Modifier.height(dimensions.space4))

            QuotifyComboboxField(
                label = stringResource(DsR.string.details_field_author),
                value = draft.authorName,
                onValueChange = { onDraftChange(draft.copy(authorName = it)) },
                onOptionSelected = { onDraftChange(draft.copy(authorName = it)) },
                options = authorOptions.matching(draft.authorName),
                placeholder = stringResource(DsR.string.details_author_ph),
            )

            Spacer(modifier = Modifier.height(dimensions.space4))

            QuotifyComboboxField(
                label = stringResource(DsR.string.details_field_book),
                value = draft.bookName,
                onValueChange = { onDraftChange(draft.copy(bookName = it)) },
                onOptionSelected = { onDraftChange(draft.copy(bookName = it)) },
                options = bookOptions.matching(draft.bookName),
                placeholder = stringResource(DsR.string.details_book_ph),
            )

            Spacer(modifier = Modifier.height(dimensions.space4))

            Column(modifier = Modifier.fillMaxWidth()) {
                QuotifyFieldLabel(text = stringResource(DsR.string.details_field_tags))
                EditableTagChips(
                    tags = draft.tags,
                    onRemoveTag = { tag ->
                        onDraftChange(draft.copy(tags = draft.tags.filterNot { it == tag }))
                    },
                    onAddTag = onOpenTagSheet,
                )
            }

            Spacer(modifier = Modifier.height(dimensions.space4))

            QuotifyPlaylistOptions(
                widgetEnabled = draft.inWidgetPlaylist,
                pushEnabled = draft.inPushPlaylist,
                onWidgetToggle = { onDraftChange(draft.copy(inWidgetPlaylist = it)) },
                onPushToggle = { onDraftChange(draft.copy(inPushPlaylist = it)) },
            )

            QuotifyButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensions.space5),
                text = stringResource(DsR.string.details_save),
                onClick = onSave,
                enabled = draft.text.isNotBlank(),
                sizeSpec = QuotifyButtonDefaults.pillSizeSpec(height = dimensions.size52),
            )
        }
    }
}

private fun List<String>.matching(query: String): List<String> {
    val trimmed = query.trim()
    return if (trimmed.isEmpty()) this else filter { it.contains(trimmed, ignoreCase = true) }
}
