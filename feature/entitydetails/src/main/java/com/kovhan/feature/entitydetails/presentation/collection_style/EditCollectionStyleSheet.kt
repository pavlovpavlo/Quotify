package com.kovhan.feature.entitydetails.presentation.collection_style

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.bottomsheet.QuotifyBottomSheet
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
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
    onToneChange: (String) -> Unit,
    onIconChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    QuotifyBottomSheet(
        onDismiss = onDismiss,
        sheetState = sheetState,
        title = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensions.space5, vertical = dimensions.space4),
        ) {
            Text(
                text = stringResource(DsR.string.collection_details_style_title),
                style = typography.h4,
                color = colors.textPrimary,
            )

            Spacer(modifier = Modifier.height(dimensions.space4))

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
                        .horizontalScroll(rememberScrollState())
                        .padding(top = dimensions.size2, bottom = dimensions.size4),
                    horizontalArrangement = Arrangement.spacedBy(dimensions.size14),
                ) {
                    CollectionColorMapper.tones.forEach { tone ->
                        val swatchColor = CollectionColorMapper.toColor(tone, colors)
                        Box(
                            modifier = Modifier
                                .size(dimensions.size52)
                                .clip(CircleShape)
                                .background(swatchColor)
                                .border(
                                    width = if (draft.tone == tone) dimensions.size2 else dimensions.size1,
                                    color = if (draft.tone == tone) colors.textPrimary else Color.Transparent,
                                    shape = CircleShape,
                                )
                                .clickable { onToneChange(tone) },
                            contentAlignment = Alignment.Center,
                        ) {
                            if (draft.tone == tone) {
                                Image(
                                    modifier = Modifier.size(dimensions.size22),
                                    painter = painterResource(DsR.drawable.ic_check),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(colors.textOnAccent),
                                )
                            }
                        }
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
    }
}

