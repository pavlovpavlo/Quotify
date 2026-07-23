package com.kovhan.core.ui.component.quote

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import com.kovhan.core.ui.component.text.QuotifyFieldLabel
import com.kovhan.core.ui.component.text_field.QuotifyOutlinedTextField
import com.kovhan.design.systems.QuotifyMaterialTheme

/**
 * Multi-line quote text area with an eyebrow label — shared by the "Add quote"
 * text/voice tabs and the quote edit sheet.
 */
@Composable
fun QuoteTextInputField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    minHeight: Dp = QuotifyMaterialTheme.dimensions.size130,
    requestFocus: Boolean = false,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(requestFocus) {
        if (requestFocus) focusRequester.requestFocus()
    }

    val selectionColors = TextSelectionColors(
        handleColor = colors.accentPrimary,
        backgroundColor = colors.accentPrimarySoft,
    )

    Column(modifier = modifier.fillMaxWidth()) {
        QuotifyFieldLabel(text = label)

        QuotifyOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight)
                .focusRequester(focusRequester),
            value = value,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(dimensions.radiusXl),
            textStyle = typography.readingBody.copy(color = colors.textPrimary),
            singleLine = false,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = colors.borderStrong,
                focusedBorderColor = colors.accentPrimary,
                cursorColor = colors.accentPrimary,
                selectionColors = selectionColors,
                unfocusedContainerColor = colors.bgElevated,
                focusedContainerColor = colors.bgElevated,
            ),
            placeholder = if (placeholder.isNotEmpty()) {
                {
                    Text(
                        text = placeholder,
                        style = typography.readingBody.copy(fontStyle = FontStyle.Italic),
                        color = colors.textTertiary,
                    )
                }
            } else {
                null
            },
        )
    }
}
