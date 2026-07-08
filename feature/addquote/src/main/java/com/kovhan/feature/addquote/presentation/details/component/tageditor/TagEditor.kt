package com.kovhan.feature.addquote.presentation.details.component.tageditor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TagEditor(
    tags: List<String>,
    onRemoveTag: (String) -> Unit,
    onAddTag: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(bottom = dimensions.space2, start = dimensions.size2),
            text = stringResource(R.string.details_field_tags),
            style = typography.eyebrow,
            color = colors.textTertiary,
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensions.space2),
            verticalArrangement = Arrangement.spacedBy(dimensions.space2),
        ) {
            tags.forEach { tag ->
                TagChip(tag = tag, onRemove = { onRemoveTag(tag) })
            }
            TagAddPill(onClick = onAddTag)
        }
    }
}
