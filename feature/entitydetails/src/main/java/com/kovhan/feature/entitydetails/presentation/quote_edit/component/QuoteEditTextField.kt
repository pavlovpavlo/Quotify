package com.kovhan.feature.entitydetails.presentation.quote_edit.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.ui.component.quote.QuoteTextInputField
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

/**
 * String-backed adapter over the shared [QuoteTextInputField]: keeps the caret
 * at the end whenever the text is replaced from outside the field.
 */
@Composable
internal fun QuoteEditTextField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    var field by remember { mutableStateOf(TextFieldValue(value, TextRange(value.length))) }

    LaunchedEffect(value) {
        if (value != field.text) {
            field = TextFieldValue(value, TextRange(value.length))
        }
    }

    QuoteTextInputField(
        value = field,
        onValueChange = {
            field = it
            onValueChange(it.text)
        },
        label = stringResource(DsR.string.details_field_quote),
        placeholder = stringResource(DsR.string.details_quote_ph),
        minHeight = QuotifyMaterialTheme.dimensions.size96,
    )
}
