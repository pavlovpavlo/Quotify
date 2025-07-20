package com.kovhan.core.ui.component.text_field

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.kovhan.core.ui.extensions.isNotNull
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
fun QuotifyTextField(
    modifier: Modifier = Modifier,
    modifierContainer: Modifier = Modifier,
    modifierError: Modifier = Modifier,
    value: TextFieldValue,
    hint: String = "",
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    mask: VisualTransformation = VisualTransformation.None,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusChange: (Boolean) -> Unit = {},
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = true,
    textStyle: TextStyle = QuotifyMaterialTheme.typography.smallLabelNormal,
    @StringRes error: Int? = null,
) {
    val focus = remember { mutableStateOf(false) }
    if (value.text.isNotEmpty()) {
        focus.value = true
    }

    val textSelectionColors = TextSelectionColors(
        handleColor = Color.Transparent,
        backgroundColor = QuotifyMaterialTheme.colors.textFieldBg,
    )

    Column(
        modifier = modifierContainer
    ) {
        QuotifyOutlinedTextField(
            modifier = modifier
                .padding(top = QuotifyMaterialTheme.dimensions.space_8)
                .height(QuotifyMaterialTheme.dimensions.size_56)
                .onFocusChanged {
                    if (focus.value != it.isFocused) {
                        focus.value = it.isFocused
                    }
                    onFocusChange(it.isFocused)
                },
            value = value,
            enabled = enabled,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.corner_radius_20),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = if (value.text.isNotBlank()) {
                    QuotifyMaterialTheme.colors.tabBg
                } else {
                    QuotifyMaterialTheme.colors.tabBg.copy(
                        alpha = QuotifyMaterialTheme.alpha.alpha_12
                    )
                },
                focusedBorderColor = QuotifyMaterialTheme.colors.tabBg,
                disabledBorderColor = QuotifyMaterialTheme.colors.tabBg,
                errorBorderColor = QuotifyMaterialTheme.colors.textColorError,
                cursorColor = QuotifyMaterialTheme.colors.textColorPrimary,
                selectionColors = textSelectionColors,
            ),
            isError = error.isNotNull(),
            visualTransformation = mask,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            label = {
                Text(
                    text = hint,
                    style = if (focus.value || value.text.isNotEmpty()) {
                        QuotifyMaterialTheme.typography.smallLabelNormal
                    } else {
                        QuotifyMaterialTheme.typography.titleNormal
                    },
                    color = if (error.isNotNull()) {
                        QuotifyMaterialTheme.colors.textColorError
                    } else {
                        QuotifyMaterialTheme.colors.textColorGray
                    }
                )
            },
            textStyle = textStyle.copy(
                color = QuotifyMaterialTheme.colors.textColorPrimary,
            ),
            minLines = minLines,
            maxLines = maxLines,
            singleLine = singleLine,
        )

        if (error != null) {
            Text(
                modifier = modifierError
                    .padding(
                        vertical = QuotifyMaterialTheme.dimensions.space_4,
                        horizontal = QuotifyMaterialTheme.dimensions.space_16
                    )
                    .fillMaxWidth(),
                text = stringResource(id = error),
                color = QuotifyMaterialTheme.colors.textColorError,
                style = QuotifyMaterialTheme.typography.smallLabelNormal
            )
        }
    }

}