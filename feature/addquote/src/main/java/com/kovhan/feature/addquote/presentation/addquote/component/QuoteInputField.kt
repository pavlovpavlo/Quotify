package com.kovhan.feature.addquote.presentation.addquote.component

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
import com.kovhan.core.ui.component.text_field.QuotifyOutlinedTextField
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
internal fun QuoteInputField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
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
        Text(
            modifier = Modifier.padding(bottom = dimensions.space2, start = dimensions.size2),
            text = label,
            style = typography.eyebrow,
            color = colors.textTertiary,
        )

        QuotifyOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = dimensions.size130)
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
