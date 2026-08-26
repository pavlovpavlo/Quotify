package com.kovhan.core.ui.component.combobox

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.kovhan.core.ui.component.text_field.QuotifyOutlinedTextField
import com.kovhan.design.systems.QuotifyMaterialTheme

private val MenuGap = 6.dp
private val MenuRowHeight = 44.dp
private val MenuVerticalPadding = 6.dp
private const val MENU_MAX_VISIBLE_ROWS = 5

/**
 * Single-select combobox with free text. The [value] is editable; while the
 * field is focused a list of [options] (already filtered by the caller) hangs
 * under it. Picking an option calls [onOptionSelected]; when nothing matches,
 * [emptyText] is shown instead — the typed text becomes a new value.
 *
 * The list is a normal composable, not a popup window: it scrolls together with
 * the form and is laid out with zero height, so opening it never pushes the rest
 * of the fields down — it simply draws over them.
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
    imeAction: ImeAction = ImeAction.Done,
    onFocusChanged: (Boolean) -> Unit = {},
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val focusManager = LocalFocusManager.current

    var focused by remember { mutableStateOf(false) }
    var field by remember { mutableStateOf(TextFieldValue(value)) }
    LaunchedEffect(value) {
        if (value != field.text) field = TextFieldValue(value, TextRange(value.length))
    }

    Column(modifier = modifier) {
        QuotifyOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    focused = it.isFocused
                    onFocusChanged(it.isFocused)
                },
            value = field,
            onValueChange = {
                field = it
                onValueChange(it.text)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = imeAction),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() },
                onNext = { focusManager.moveFocus(FocusDirection.Next) },
            ),
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

        if (focused) {
            ComboboxSuggestions(
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ComboboxSuggestions(
    options: List<String>,
    emptyText: String,
    onOptionSelected: (String) -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val density = LocalDensity.current
    val shape = RoundedCornerShape(dimensions.radiusXl)

    val visibleRows = options.size.coerceIn(1, MENU_MAX_VISIBLE_ROWS)
    val cardHeight: Dp = MenuRowHeight * visibleRows + MenuVerticalPadding * 2
    val gapPx = with(density) { MenuGap.roundToPx() }

    val bringIntoView = remember { BringIntoViewRequester() }
    LaunchedEffect(cardHeight) {
        // Обгортка має нульову висоту, тому просимо скрол відкрити саму картку —
        // її координати справжні, і список унизу форми не лишиться за кадром.
        bringIntoView.bringIntoView()
    }

    Box(
        modifier = Modifier
            .zIndex(1f)
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                // Нуль по висоті — список не займає місця в колонці, тому
                // сусідні поля не зсуваються, коли він зʼявляється.
                layout(placeable.width, 0) { placeable.place(0, gapPx) }
            }
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = cardHeight)
                .bringIntoViewRequester(bringIntoView)
                .shadow(8.dp, shape)
                .clip(shape)
                .background(colors.bgElevated)
                .border(dimensions.size1, colors.border, shape)
                .verticalScroll(rememberScrollState())
                .padding(vertical = MenuVerticalPadding),
        ) {
            if (options.isEmpty()) {
                SuggestionRow(text = emptyText, muted = true, onClick = null)
            } else {
                options.forEach { option ->
                    SuggestionRow(
                        text = option,
                        muted = false,
                        onClick = { onOptionSelected(option) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SuggestionRow(
    text: String,
    muted: Boolean,
    onClick: (() -> Unit)?,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(MenuRowHeight)
            .let { if (onClick != null) it.clickable(onClick = onClick) else it }
            .padding(horizontal = dimensions.space4),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = text,
            style = if (muted) typography.caption else typography.body,
            color = if (muted) colors.textTertiary else colors.textPrimary,
        )
    }
}
