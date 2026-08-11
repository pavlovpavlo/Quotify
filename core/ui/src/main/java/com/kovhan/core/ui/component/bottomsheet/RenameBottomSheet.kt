package com.kovhan.core.ui.component.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.text_field.QuotifyTextField
import com.kovhan.design.systems.QuotifyMaterialTheme
import kotlinx.coroutines.delay

/**
 * Reusable "rename" bottom sheet: a single autofocused text field capped at
 * [maxLength] plus a confirm button. All labels are passed in so the same sheet
 * works for collections, tags, books, authors and any other named entity.
 *
 * [onBack] adds the leading back chevron — pass it only when the sheet is a step
 * inside a longer flow, not when it edits something that already exists.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RenameBottomSheet(
    title: String,
    label: String,
    placeholder: String,
    initialName: String,
    confirmText: String,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit,
    maxLength: Int = 60,
    onBack: (() -> Unit)? = null,
) {

    val dimensions = QuotifyMaterialTheme.dimensions
    var field by remember {
        mutableStateOf(TextFieldValue(initialName, TextRange(initialName.length)))
    }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(initialName) {
        if (initialName != field.text) {
            field = TextFieldValue(initialName, TextRange(initialName.length))
        }
    }

    LaunchedEffect(Unit) {
        delay(150)
        runCatching { focusRequester.requestFocus() }
    }

    QuotifyBottomSheet(
        onDismiss = onDismiss,
        title = title,
        onBack = onBack,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = dimensions.space5, end = dimensions.space5, bottom = dimensions.space4),
        ) {
            QuotifyTextField(
                value = field,
                onValueChange = { field = it.copy(text = it.text.take(maxLength)) },
                label = label,
                placeholder = placeholder,
                singleLine = true,
                modifier = Modifier.focusRequester(focusRequester),
                modifierContainer = Modifier.fillMaxWidth(),
            )

            QuotifyButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensions.space4),
                text = confirmText,
                onClick = { onSave(field.text) },
                enabled = field.text.trim().isNotEmpty(),
                sizeSpec = QuotifyButtonDefaults.pillSizeSpec(height = dimensions.size52),
            )
        }
    }
}
