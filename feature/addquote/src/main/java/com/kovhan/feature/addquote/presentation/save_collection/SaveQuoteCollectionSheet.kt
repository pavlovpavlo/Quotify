package com.kovhan.feature.addquote.presentation.save_collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kovhan.core.models.SavedCollection
import com.kovhan.core.ui.component.bottomsheet.CollectionPickerSheet
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

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
    CollectionPickerSheet(
        title = stringResource(R.string.details_save_collection_title),
        collections = collections,
        selectedCollectionId = chosenCollectionId,
        addContentDescription = stringResource(R.string.details_save_collection_add_cd),
        onSelectCollection = onChooseCollection,
        onAddToCollection = onSaveToCollection,
        onDismiss = onDismiss,
        busyCollectionId = savingCollectionId,
        footer = { CreateCollectionButton(onClick = onCreateCollection) },
    )
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
