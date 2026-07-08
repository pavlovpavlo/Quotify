package com.kovhan.core.ui.component.combobox

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.kovhan.core.ui.component.text_field.QuotifyOutlinedTextField
import com.kovhan.design.systems.QuotifyMaterialTheme

/**
 * Single-select combobox with free text. The [value] is editable; while the
 * field is focused a dropdown of [options] (already filtered by the caller)
 * floats below it. Picking an option calls [onOptionSelected]; when nothing
 * matches, [emptyText] is shown instead — the typed text becomes a new value.
 */
@Composable
fun QuotifyCombobox(
    value: String,
    onValueChange: (String) -> Unit,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    placeholder: String,
    emptyText: String,
    modifier: Modifier = Modifier,
    leadingIcon: Painter? = null,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val density = LocalDensity.current
    val focusManager = LocalFocusManager.current

    var focused by remember { mutableStateOf(false) }
    var anchorSize by remember { mutableStateOf(IntSize.Zero) }
    var field by remember { mutableStateOf(TextFieldValue(value)) }
    LaunchedEffect(value) {
        if (value != field.text) field = TextFieldValue(value, TextRange(value.length))
    }

    Box(modifier = modifier) {
        QuotifyOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { anchorSize = it }
                .onFocusChanged { focused = it.isFocused },
            value = field,
            onValueChange = {
                field = it
                onValueChange(it.text)
            },
            singleLine = true,
            shape = RoundedCornerShape(dimensions.radiusXl),
            textStyle = typography.body.copy(color = colors.textPrimary),
            leadingIcon = leadingIcon?.let {
                {
                    Image(
                        modifier = Modifier.size(dimensions.iconMd),
                        painter = it,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(colors.textTertiary),
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = colors.borderStrong,
                focusedBorderColor = colors.accentPrimary,
                cursorColor = colors.accentPrimary,
                unfocusedContainerColor = colors.bgElevated,
                focusedContainerColor = colors.bgElevated,
            ),
            placeholder = {
                Text(
                    text = placeholder,
                    style = typography.body,
                    color = colors.textTertiary,
                )
            },
        )

        if (focused && anchorSize != IntSize.Zero) {
            Popup(
                offset = IntOffset(0, anchorSize.height + with(density) { 6.dp.roundToPx() }),
                properties = PopupProperties(focusable = false),
            ) {
                ComboboxMenu(
                    width = with(density) { anchorSize.width.toDp() },
                    options = options,
                    emptyText = emptyText,
                    onOptionSelected = {
                        onOptionSelected(it)
                        focusManager.clearFocus()
                    },
                )
            }
        }
    }
}

@Composable
private fun ComboboxMenu(
    width: Dp,
    options: List<String>,
    emptyText: String,
    onOptionSelected: (String) -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val shape = RoundedCornerShape(dimensions.radiusXl)

    Column(
        modifier = Modifier
            .width(width)
            .clip(shape)
            .background(colors.bgElevated)
            .border(dimensions.size1, colors.border, shape)
            .heightIn(max = dimensions.size320)
            .verticalScroll(rememberScrollState())
            .padding(vertical = dimensions.space2),
    ) {
        if (options.isEmpty()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensions.space4, vertical = dimensions.space3),
                text = emptyText,
                style = typography.caption,
                color = colors.textTertiary,
            )
        } else {
            options.forEach { option ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOptionSelected(option) }
                        .padding(horizontal = dimensions.space4, vertical = dimensions.space3),
                    text = option,
                    style = typography.body,
                    color = colors.textPrimary,
                )
            }
        }
    }
}
