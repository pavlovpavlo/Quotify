package com.kovhan.feature.entitydetails.presentation.collection_style

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.bottomsheet.QuotifyBottomSheet
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.color.QuotifyColorSwatch
import com.kovhan.core.ui.component.premium.PremiumLockOverlay
import com.kovhan.core.ui.component.premium.premiumBlur
import com.kovhan.core.ui.component.text.QuotifyFieldLabel
import com.kovhan.core.ui.mapper.CollectionColorMapper
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.entitydetails.presentation.collection_style.component.CollectionStyleIconGrid
import com.kovhan.feature.entitydetails.presentation.collection_style.model.CollectionStyleDraft
import com.kovhan.design.systems.R as DsR

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun EditCollectionStyleSheet(
    draft: CollectionStyleDraft,
    locked: Boolean,
    onToneChange: (String) -> Unit,
    onIconChange: (String) -> Unit,
    onSave: () -> Unit,
    onUnlock: () -> Unit,
    onDismiss: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    QuotifyBottomSheet(
        onDismiss = onDismiss,
        sheetState = sheetState,
        title = stringResource(DsR.string.collection_details_style_title),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (locked) Modifier.premiumBlur() else Modifier)
                .padding(
                    start = dimensions.space5,
                    end = dimensions.space5,
                    bottom = dimensions.space4
                ),
        ) {
            Column(
                modifier = Modifier
                    .heightIn(max = 340.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                QuotifyFieldLabel(text = stringResource(DsR.string.collection_details_color_label))

                Spacer(modifier = Modifier.height(dimensions.space3))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimensions.size2, bottom = dimensions.size4),
                    horizontalArrangement = Arrangement.spacedBy(dimensions.size7),
                ) {
                    CollectionColorMapper.tones.forEach { tone ->
                        QuotifyColorSwatch(
                            modifier = Modifier.weight(1f),
                            color = CollectionColorMapper.toColor(tone, colors),
                            selected = draft.tone == tone,
                            onClick = { onToneChange(tone) },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(dimensions.space5))

                QuotifyFieldLabel(text = stringResource(DsR.string.collection_details_icon_label))

                Spacer(modifier = Modifier.height(dimensions.space3))

                CollectionStyleIconGrid(
                    selectedIconId = draft.iconId,
                    selectedTone = draft.tone,
                    onIconChange = onIconChange,
                )
            }

            QuotifyButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensions.space5),
                text = stringResource(DsR.string.details_save),
                onClick = onSave,
                sizeSpec = QuotifyButtonDefaults.pillSizeSpec(height = dimensions.size52),
            )
        }

            if (locked) PremiumLockOverlay(onUnlock = onUnlock)
        }
    }
}

