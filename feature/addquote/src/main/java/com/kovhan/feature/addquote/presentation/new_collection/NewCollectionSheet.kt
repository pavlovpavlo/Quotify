package com.kovhan.feature.addquote.presentation.new_collection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.ui.component.bottomsheet.QuotifyBottomSheet
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.text_field.QuotifyTextField
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NewCollectionSheet(
    name: String,
    isSaving: Boolean,
    onNameChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onClose: () -> Unit,
) {
    val dimensions = QuotifyMaterialTheme.dimensions
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(150)
        runCatching { focusRequester.requestFocus() }
    }

    QuotifyBottomSheet(
        onDismiss = onClose,
        sheetState = sheetState,
        title = stringResource(R.string.details_new_collection_title),
        onBack = onBack,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = dimensions.space5, end = dimensions.space5, bottom = dimensions.space5),
        ) {
            QuotifyTextField(
                value = TextFieldValue(name, TextRange(name.length)),
                onValueChange = { onNameChange(it.text) },
                label = stringResource(R.string.details_new_collection_name),
                placeholder = stringResource(R.string.details_new_collection_placeholder),
                singleLine = true,
                modifier = Modifier.focusRequester(focusRequester),
                modifierContainer = Modifier.fillMaxWidth(),
            )

            QuotifyButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensions.space4),
                text = stringResource(R.string.details_save),
                onClick = onSave,
                enabled = name.trim().isNotEmpty() && !isSaving,
                loading = isSaving,
                sizeSpec = QuotifyButtonDefaults.pillSizeSpec(height = dimensions.size52),
            )
        }
    }
}
