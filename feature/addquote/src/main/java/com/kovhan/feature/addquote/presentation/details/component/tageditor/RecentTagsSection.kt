package com.kovhan.feature.addquote.presentation.details.component.tageditor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun RecentTagsSection(
    tags: List<String>,
    isSelected: (String) -> Boolean,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (tags.isEmpty()) return

    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(bottom = dimensions.space3, start = dimensions.size2),
            text = stringResource(R.string.details_recent_eyebrow),
            style = typography.eyebrow,
            color = colors.textTertiary,
        )
        TagCloud(
            tags = tags,
            accent = TagOptionAccent.Recent,
            isSelected = isSelected,
            onToggle = onToggle,
        )
    }
}
