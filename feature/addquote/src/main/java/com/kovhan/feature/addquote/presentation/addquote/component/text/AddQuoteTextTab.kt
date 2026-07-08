package com.kovhan.feature.addquote.presentation.addquote.component.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.design.systems.R
import com.kovhan.feature.addquote.presentation.addquote.component.QuoteInputField

@Composable
internal fun AddQuoteTextTab(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    requestFocus: Boolean = false,
) {
    QuoteInputField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = stringResource(R.string.add_quote_field_label),
        placeholder = stringResource(R.string.add_quote_field_placeholder),
        requestFocus = requestFocus,
    )
}
