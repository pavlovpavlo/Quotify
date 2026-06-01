package com.kovhan.core.ui.component.text_field

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.extensions.isNotNull
import com.kovhan.design.systems.QuotifyMaterialTheme

/**
 * Standard Quotify field — an uppercase eyebrow [label] above the input,
 * an optional red [required] asterisk next to it, the actual rounded-pill
 * input with [placeholder] text inside, and an optional inline [error]
 * message underneath.
 *
 * Material's floating-label behavior is intentionally avoided — the design
 * uses a static label so focusing the field doesn't shift it up and out.
 */
@Composable
fun QuotifyTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    modifierContainer: Modifier = Modifier,
    modifierError: Modifier = Modifier,
    label: String? = null,
    required: Boolean = false,
    placeholder: String = "",
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    mask: VisualTransformation = VisualTransformation.None,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = true,
    textStyle: TextStyle = QuotifyMaterialTheme.typography.body,
    @StringRes error: Int? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val colors = QuotifyMaterialTheme.colors
    val typography = QuotifyMaterialTheme.typography
    val dimens = QuotifyMaterialTheme.dimensions

    val textSelectionColors = TextSelectionColors(
        handleColor = colors.accentPrimary,
        backgroundColor = colors.accentPrimarySoft,
    )

    Column(modifier = modifierContainer.fillMaxWidth()) {
        if (label != null) {
            Row(
                modifier = Modifier.padding(bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = label,
                    style = typography.eyebrow,
                    color = colors.textSecondary,
                )
                if (required) {
                    Text(
                        text = "*",
                        style = typography.eyebrow,
                        color = colors.error,
                    )
                }
            }
        }

        QuotifyOutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .height(dimens.textFieldHeight),
            value = value,
            enabled = enabled,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(dimens.radiusLg),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = colors.borderStrong,
                focusedBorderColor = colors.accentPrimary,
                disabledBorderColor = colors.borderSubtle,
                errorBorderColor = colors.error,
                cursorColor = colors.accentPrimary,
                selectionColors = textSelectionColors,
                unfocusedContainerColor = colors.bgElevated,
                focusedContainerColor = colors.bgElevated,
                disabledContainerColor = colors.bgSecondary,
                errorContainerColor = colors.bgElevated,
            ),
            isError = error.isNotNull(),
            visualTransformation = mask,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            placeholder = if (placeholder.isNotEmpty()) {
                {
                    Text(
                        text = placeholder,
                        style = textStyle,
                        color = colors.textTertiary,
                    )
                }
            } else null,
            textStyle = textStyle.copy(color = colors.textPrimary),
            minLines = minLines,
            maxLines = maxLines,
            singleLine = singleLine,
            trailingIcon = trailingIcon,
        )

        if (error != null) {
            Text(
                modifier = modifierError
                    .padding(
                        start = dimens.space3,
                        end = dimens.space3,
                        top = dimens.space1,
                    )
                    .fillMaxWidth(),
                text = stringResource(id = error),
                color = colors.error,
                style = typography.caption,
            )
        }
    }
}
