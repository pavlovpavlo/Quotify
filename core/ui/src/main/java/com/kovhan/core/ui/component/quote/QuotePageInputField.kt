package com.kovhan.core.ui.component.quote

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.ui.component.text.QuotifyFieldLabel
import com.kovhan.core.ui.component.text_field.QuotifyOutlinedTextField
import com.kovhan.design.systems.QuotifyMaterialTheme

/**
 * Optional page number the quote was taken from — a bare labelled field with no
 * placeholder, opening the numeric keyboard. Non-digits are dropped as typed, so
 * the value is always parseable.
 */
@Composable
fun QuotePageInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    var field by remember { mutableStateOf(TextFieldValue(value)) }
    LaunchedEffect(value) {
        if (value != field.text) field = TextFieldValue(value, TextRange(value.length))
    }

    Column(modifier = modifier.fillMaxWidth()) {
        QuotifyFieldLabel(text = label)

        QuotifyOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = field,
            onValueChange = { input ->
                val digits = input.text.filter(Char::isDigit).take(MAX_DIGITS)
                val caret = input.selection.end.coerceAtMost(digits.length)
                field = input.copy(text = digits, selection = TextRange(caret))
                onValueChange(digits)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(dimensions.radiusXl),
            textStyle = typography.body.copy(color = colors.textPrimary),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = colors.borderStrong,
                focusedBorderColor = colors.accentPrimary,
                cursorColor = colors.accentPrimary,
                unfocusedContainerColor = colors.bgElevated,
                focusedContainerColor = colors.bgElevated,
            ),
        )
    }
}

private const val MAX_DIGITS = 6
