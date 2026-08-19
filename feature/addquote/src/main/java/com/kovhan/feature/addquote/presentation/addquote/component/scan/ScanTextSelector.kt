package com.kovhan.feature.addquote.presentation.addquote.component.scan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.models.quote.QuoteLimits
import com.kovhan.core.ui.component.text_field.QuotifyOutlinedTextField
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R


@Composable
internal fun ScanTextSelector(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    val selectionColors = TextSelectionColors(
        handleColor = colors.accentPrimary,
        backgroundColor = colors.accentPrimary.copy(alpha = 0.4f),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensions.space5),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensions.space3),
            text = stringResource(R.string.add_quote_scan_caption_select),
            style = typography.caption,
            color = colors.textTertiary,
        )

        QuotifyOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .focusRequester(focusRequester),
            value = value,
            onValueChange = { input ->
                onValueChange(
                    if (input.text.length <= QuoteLimits.MAX_TEXT_LENGTH) {
                        input
                    } else {
                        input.copy(text = input.text.take(QuoteLimits.MAX_TEXT_LENGTH))
                    },
                )
            },
            shape = RoundedCornerShape(dimensions.radiusXl),
            textStyle = typography.readingBody.copy(color = colors.textPrimary),
            singleLine = false,
            selectionColors = selectionColors,
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
