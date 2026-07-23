package com.kovhan.feature.main.presentation.quotes.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.ui.component.emptystate.DefaultEmptyState
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

private const val COLUMNS = 2

@Composable
internal fun LibraryFoldersSection(
    folders: List<SavedCollection>,
    onOpenFolder: (String) -> Unit,
    onCreateFolder: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    Column(modifier = modifier.fillMaxWidth()) {
        if (folders.isEmpty()) {
            DefaultEmptyState(
                imageResource = QuotifyMaterialTheme.images.imgLibraryEmpty,
                titleResource = R.string.library_empty_title,
                descriptionResource = R.string.library_empty_description,
            )
        } else {
            Text(
                modifier = Modifier.padding(
                    start = dimensions.size2,
                    bottom = dimensions.size12,
                ),
                text = stringResource(R.string.library_folders_eyebrow),
                style = typography.eyebrow,
                color = colors.textTertiary,
            )

            val cellCount = folders.size + 1
            val rowCount = (cellCount + COLUMNS - 1) / COLUMNS

            for (row in 0 until rowCount) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(top = if (row == 0) dimensions.space0 else dimensions.size12),
                    horizontalArrangement = Arrangement.spacedBy(dimensions.size12),
                ) {
                    for (col in 0 until COLUMNS) {
                        val index = row * COLUMNS + col
                        when {
                            index < folders.size -> {
                                val folder = folders[index]
                                FolderCard(
                                    collection =
                                        folder,
                                    onClick = { onOpenFolder(folder.id) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                )
                            }

                            index == folders.size -> NewFolderTile(
                                onClick = onCreateFolder,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                            )

                            else -> Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
