package com.kovhan.feature.entitydetails.presentation.move_quote

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.ui.component.bottomsheet.CollectionPickerSheet
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

/** Bottom sheet to move a quote into a collection. Shared by details screens. */
@Composable
internal fun EntityMoveQuoteSheet(
    targets: List<SavedCollection>,
    selectedCollectionId: String?,
    onSelectCollection: (String) -> Unit,
    onAddToCollection: (String) -> Unit,
    onCreateCollection: () -> Unit,
    onDismiss: () -> Unit,
) {
    val dimensions = QuotifyMaterialTheme.dimensions

    CollectionPickerSheet(
        title = stringResource(DsR.string.collection_details_move_title),
        collections = targets,
        selectedCollectionId = selectedCollectionId,
        addContentDescription = stringResource(DsR.string.collection_details_move_add_cd),
        onSelectCollection = onSelectCollection,
        onAddToCollection = onAddToCollection,
        onDismiss = onDismiss,
        countText = { count ->
            pluralStringResource(DsR.plurals.library_folder_quote_count, count, count)
        },
        footer = {
            QuotifyButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(DsR.string.details_create_collection),
                onClick = onCreateCollection,
                variant = QuotifyButtonVariant.Outlined,
                accent = QuotifyButtonAccent.Primary,
                leadingIcon = painterResource(DsR.drawable.ic_plus),
                sizeSpec = QuotifyButtonDefaults.pillSizeSpec(height = dimensions.size52),
            )
        },
    )
}
