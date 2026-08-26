package com.kovhan.core.ui.component.combobox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.zIndex
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
    imeAction: ImeAction = ImeAction.Done,
) {
    // Список підказок висить поверх сусідніх полів, а порядок малювання
    // визначається на рівні батьківської колонки — тож поле у фокусі підіймаємо
    // над рештою. Сфокусоване поле завжди одне, тому конфлікту z-порядку немає.
    var focused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .zIndex(if (focused) 1f else 0f),
    ) {
        QuotifyFieldLabel(text = label)
        QuotifyCombobox(
            value = value,
            onValueChange = onValueChange,
            options = options,
            onOptionSelected = onOptionSelected,
            placeholder = placeholder,
            emptyText = emptyText,
            imeAction = imeAction,
            onFocusChanged = { focused = it },
        )
    }
}
