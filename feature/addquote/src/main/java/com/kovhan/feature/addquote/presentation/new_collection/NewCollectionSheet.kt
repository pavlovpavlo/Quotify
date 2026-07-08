package com.kovhan.feature.addquote.presentation.new_collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.ui.component.bottomsheet.QuotifyBottomSheet
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.core.ui.component.button.QuotifyIconButton
import com.kovhan.core.ui.component.text_field.QuotifyTextField
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

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
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    QuotifyBottomSheet(
        onDismiss = onClose,
        sheetState = sheetState,
        title = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = dimensions.space5, end = dimensions.space5, bottom = dimensions.space5),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensions.space4),
            ) {
                Row(
                    modifier = Modifier.align(Alignment.CenterStart),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    QuotifyIconButton(
                        onClick = onBack,
                        variant = QuotifyButtonVariant.Ghost,
                        accent = QuotifyButtonAccent.Neutral,
                        size = dimensions.iconXl,
                        debounceInterval = 0L,
                    ) {
                        CompositionLocalProvider(LocalContentColor provides colors.textSecondary) {
                            Image(
                                modifier = Modifier.size(dimensions.size18),
                                painter = painterResource(R.drawable.ic_back),
                                contentDescription = stringResource(R.string.details_back_cd),
                                colorFilter = ColorFilter.tint(LocalContentColor.current),
                            )
                        }
                    }
                }
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.details_new_collection_title),
                    style = typography.h4,
                    color = colors.textPrimary,
                )
                QuotifyIconButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = onClose,
                    variant = QuotifyButtonVariant.Ghost,
                    accent = QuotifyButtonAccent.Neutral,
                    size = dimensions.iconXl,
                    debounceInterval = 0L,
                ) {
                    CompositionLocalProvider(LocalContentColor provides colors.textSecondary) {
                        Image(
                            modifier = Modifier.size(dimensions.size18),
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = stringResource(R.string.add_quote_close_cd),
                            colorFilter = ColorFilter.tint(LocalContentColor.current),
                        )
                    }
                }
            }

            QuotifyTextField(
                value = TextFieldValue(name, TextRange(name.length)),
                onValueChange = { onNameChange(it.text) },
                label = stringResource(R.string.details_new_collection_name),
                placeholder = stringResource(R.string.details_new_collection_placeholder),
                singleLine = true,
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
