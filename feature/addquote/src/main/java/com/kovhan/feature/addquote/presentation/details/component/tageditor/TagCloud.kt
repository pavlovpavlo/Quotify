package com.kovhan.feature.addquote.presentation.details.component.tageditor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kovhan.design.systems.QuotifyMaterialTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TagCloud(
    tags: List<String>,
    accent: TagOptionAccent,
    isSelected: (String) -> Boolean,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = QuotifyMaterialTheme.dimensions

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensions.space2),
        verticalArrangement = Arrangement.spacedBy(dimensions.space2),
    ) {
        tags.forEach { tag ->
            TagOption(
                label = tag,
                selected = isSelected(tag),
                accent = accent,
                onClick = { onToggle(tag) },
            )
        }
    }
}
