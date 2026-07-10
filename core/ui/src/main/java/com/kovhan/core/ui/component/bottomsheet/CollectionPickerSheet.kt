package com.kovhan.core.ui.component.bottomsheet

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
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.core.ui.component.button.QuotifyIconButton
import com.kovhan.core.ui.mapper.CollectionIconMapper
import com.kovhan.core.ui.mapper.collectionColor
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

/**
 * Bottom sheet listing collections for the user to pick one and add the current
 * quote into it. Shared by "save to collection" (add-quote flow) and "move to
 * collection" (details flow); the small differences between them are exposed as
 * parameters:
 *
 * - [busyCollectionId] shows a loading spinner on that row's add button.
 * - [showSelectedCheck] renders a check mark on the selected row.
 * - [countText] formats the per-collection quote count.
 * - [footer] is the trailing "create collection" affordance.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CollectionPickerSheet(
    title: String,
    collections: List<SavedCollection>,
    selectedCollectionId: String?,
    addContentDescription: String,
    onSelectCollection: (String) -> Unit,
    onAddToCollection: (String) -> Unit,
    onDismiss: () -> Unit,
    footer: @Composable () -> Unit,
    busyCollectionId: String? = null,
    showSelectedCheck: Boolean = false,
    countText: @Composable (Int) -> String = { it.toString() },
) {
    val dimensions = QuotifyMaterialTheme.dimensions
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
            SheetTitleBar(title = title, onClose = onDismiss)

            Spacer(modifier = Modifier.height(dimensions.space3))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimensions.size2),
            ) {
                collections.forEach { collection ->
                    CollectionPickerRow(
                        collection = collection,
                        selected = collection.id == selectedCollectionId,
                        loading = busyCollectionId == collection.id,
                        showCheck = showSelectedCheck,
                        countText = countText,
                        addContentDescription = addContentDescription,
                        onSelect = { onSelectCollection(collection.id) },
                        onAdd = { onAddToCollection(collection.id) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensions.space4))

            footer()
        }
    }
}

@Composable
private fun CollectionPickerRow(
    collection: SavedCollection,
    selected: Boolean,
    loading: Boolean,
    showCheck: Boolean,
    countText: @Composable (Int) -> String,
    addContentDescription: String,
    onSelect: () -> Unit,
    onAdd: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val tone = collectionColor(collection.iconColor)
    val shape = RoundedCornerShape(dimensions.size12)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .then(if (selected) Modifier.background(colors.accentSavedSoft) else Modifier)
            .clickable(onClick = onSelect)
            .padding(horizontal = dimensions.space3, vertical = dimensions.space3),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(dimensions.size40)
                .clip(RoundedCornerShape(dimensions.radiusLg))
                .background(if (selected) colors.accentSaved else tone.copy(alpha = 0.16f)),
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
        ) {
            Text(
                text = collection.name,
                style = typography.bodyStrong,
                color = colors.textPrimary,
            )
            Text(
                modifier = Modifier.padding(top = dimensions.size2),
                text = countText(collection.quoteCount ?: 0),
                style = typography.caption,
                color = colors.textTertiary,
            )
        }

        if (showCheck && selected) {
            Image(
                modifier = Modifier
                    .padding(end = dimensions.size6)
                    .size(dimensions.size22),
                painter = painterResource(DsR.drawable.ic_check),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colors.accentSaved),
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
                    modifier = Modifier.size(dimensions.size18),
                    painter = painterResource(DsR.drawable.ic_plus),
                    contentDescription = addContentDescription,
                    colorFilter = ColorFilter.tint(LocalContentColor.current),
                )
            }
        }
    }
}
