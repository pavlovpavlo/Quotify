package com.kovhan.feature.addquote.presentation.save_collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kovhan.core.models.SavedCollection
import com.kovhan.core.ui.component.bottomsheet.QuotifyBottomSheet
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.core.ui.component.button.QuotifyIconButton
import com.kovhan.core.ui.mapper.CollectionIconMapper
import com.kovhan.core.ui.mapper.collectionColor
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SaveQuoteCollectionSheet(
    collections: List<SavedCollection>,
    chosenCollectionId: String?,
    savingCollectionId: String?,
    onChooseCollection: (String) -> Unit,
    onSaveToCollection: (String) -> Unit,
    onCreateCollection: () -> Unit,
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
                .padding(start = dimensions.space5, end = dimensions.space5, bottom = dimensions.space5),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensions.space3),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.details_save_collection_title),
                    style = typography.h4,
                    color = colors.textPrimary,
                )
                QuotifyIconButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = onDismiss,
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

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimensions.size2),
            ) {
                collections.forEach { collection ->
                    CollectionRow(
                        collection = collection,
                        selected = collection.id == chosenCollectionId,
                        loading = savingCollectionId == collection.id,
                        onChoose = { onChooseCollection(collection.id) },
                        onAdd = { onSaveToCollection(collection.id) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensions.space4))

            CreateCollectionButton(onClick = onCreateCollection)
        }
    }
}

@Composable
private fun CollectionRow(
    collection: SavedCollection,
    selected: Boolean,
    loading: Boolean,
    onChoose: () -> Unit,
    onAdd: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val tone = collectionColor(collection.iconColor)
    val rowShape = RoundedCornerShape(dimensions.size12)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(rowShape)
            .then(if (selected) Modifier.background(colors.accentSavedSoft) else Modifier)
            .clickable(onClick = onChoose)
            .padding(horizontal = dimensions.space3, vertical = dimensions.space3),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(dimensions.size40)
                .background(
                    color = if (selected) colors.accentSaved else tone.copy(alpha = 0.16f),
                    shape = RoundedCornerShape(dimensions.radiusLg),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(dimensions.iconMd),
                painter = painterResource(CollectionIconMapper.toDrawableRes(collection.iconId)),
                contentDescription = null,
                colorFilter = ColorFilter.tint(if (selected) colors.textOnAccent else tone),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = dimensions.space3, end = dimensions.space3),
            verticalArrangement = Arrangement.spacedBy(dimensions.size2),
        ) {
            Text(
                text = collection.name,
                style = typography.bodyStrong,
                color = colors.textPrimary,
            )
            Text(
                text = "${collection.quoteCount ?: 0}",
                style = typography.caption,
                color = colors.textTertiary,
            )
        }

        QuotifyIconButton(
            onClick = onAdd,
            loading = loading,
            variant = QuotifyButtonVariant.Outlined,
            accent = QuotifyButtonAccent.Primary,
            size = dimensions.size40,
            debounceInterval = 0L,
        ) {
            CompositionLocalProvider(LocalContentColor provides colors.accentPrimary) {
                Image(
                    modifier = Modifier.size(dimensions.size14),
                    painter = painterResource(R.drawable.ic_plus),
                    contentDescription = stringResource(R.string.details_save_collection_add_cd),
                    colorFilter = ColorFilter.tint(LocalContentColor.current),
                )
            }
        }
    }
}

@Composable
private fun CreateCollectionButton(
    onClick: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colors.bgSecondary,
                shape = RoundedCornerShape(dimensions.radius2xl),
            )
            .clickable(onClick = onClick)
            .padding(vertical = dimensions.space4),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensions.space2),
        ) {
            Image(
                modifier = Modifier.size(dimensions.size14),
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colors.accentPrimary),
            )
            Text(
                text = stringResource(R.string.details_create_collection),
                style = typography.bodyStrong,
                color = colors.accentPrimary,
            )
        }
    }
}
