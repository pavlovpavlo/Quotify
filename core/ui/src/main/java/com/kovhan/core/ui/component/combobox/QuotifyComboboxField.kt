package com.kovhan.core.ui.component.combobox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kovhan.core.ui.component.text.QuotifyFieldLabel
import com.kovhan.design.systems.R as DsR

/**
 * Labeled free-text combobox field — an eyebrow [label] above a [QuotifyCombobox].
 * Shared by the add-quote details screen (author/book) and the quote edit sheet.
 */
@Composable
fun QuotifyComboboxField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onOptionSelected: (String) -> Unit,
    options: List<String>,
    placeholder: String,
    modifier: Modifier = Modifier,
    emptyText: String = stringResource(DsR.string.details_combo_empty),
) {
    Column(modifier = modifier.fillMaxWidth()) {
        QuotifyFieldLabel(text = label)
        QuotifyCombobox(
            value = value,
            onValueChange = onValueChange,
            options = options,
            onOptionSelected = onOptionSelected,
            placeholder = placeholder,
            emptyText = emptyText,
        )
    }
}
