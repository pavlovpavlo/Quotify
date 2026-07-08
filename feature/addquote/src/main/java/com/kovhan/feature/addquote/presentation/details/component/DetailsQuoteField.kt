package com.kovhan.feature.addquote.presentation.details.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.res.stringResource
import com.kovhan.core.ui.component.text_field.QuotifyOutlinedTextField
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun DetailsQuoteField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(bottom = dimensions.space2, start = dimensions.size2),
            text = stringResource(R.string.details_field_quote),
            style = typography.eyebrow,
            color = colors.textTertiary,
        )

        QuotifyOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = dimensions.size96),
            value = value,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(dimensions.radiusXl),
            textStyle = typography.readingBody.copy(color = colors.textPrimary),
            singleLine = false,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = colors.borderStrong,
                focusedBorderColor = colors.accentPrimary,
                cursorColor = colors.accentPrimary,
                unfocusedContainerColor = colors.bgElevated,
                focusedContainerColor = colors.bgElevated,
            ),
            placeholder = {
                Text(
                    text = stringResource(R.string.details_quote_ph),
                    style = typography.readingBody.copy(fontStyle = FontStyle.Italic),
                    color = colors.textTertiary,
                )
            },
        )
    }
}
