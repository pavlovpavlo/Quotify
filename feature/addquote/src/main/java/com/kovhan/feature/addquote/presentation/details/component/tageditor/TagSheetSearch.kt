package com.kovhan.feature.addquote.presentation.details.component.tageditor

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.ui.component.bottomsheet.rememberSheetAutofocusRequester
import com.kovhan.core.ui.component.text_field.QuotifyOutlinedTextField
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

private const val MAX_TAG_LENGTH = 20

@Composable
internal fun TagSheetSearch(
    query: String,
    onQueryChange: (String) -> Unit,
    onSubmit: () -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    val focusRequester = rememberSheetAutofocusRequester()

    var field by remember { mutableStateOf(TextFieldValue(query)) }
    LaunchedEffect(query) {
        if (query != field.text) field = TextFieldValue(query, TextRange(query.length))
    }

    QuotifyOutlinedTextField(
        modifier = modifier.focusRequester(focusRequester),
        value = field,
        onValueChange = {
            val trimmed = it.text.take(MAX_TAG_LENGTH)
            val selection = TextRange(trimmed.length)
            field = it.copy(text = trimmed, selection = selection)
            onQueryChange(trimmed)
        },
        singleLine = true,
        shape = RoundedCornerShape(dimensions.size12),
        textStyle = typography.body.copy(color = colors.textPrimary),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { onSubmit() }),
        leadingIcon = {
            Image(
                modifier = Modifier.size(dimensions.iconSm),
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colors.textTertiary),
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = colors.border,
            focusedBorderColor = colors.accentPrimary,
            cursorColor = colors.accentPrimary,
            unfocusedContainerColor = colors.bgSecondary,
            focusedContainerColor = colors.bgSecondary,
        ),
        placeholder = {
            Text(
                text = placeholder,
                style = typography.body,
                color = colors.textTertiary,
            )
        },
    )
}
