package com.kovhan.feature.addquote.presentation.details.component.tageditor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kovhan.core.ui.component.quote.EditableTagChips
import com.kovhan.core.ui.component.text.QuotifyFieldLabel
import com.kovhan.design.systems.R

@Composable
internal fun TagEditor(
    tags: List<String>,
    onRemoveTag: (String) -> Unit,
    onAddTag: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        QuotifyFieldLabel(text = stringResource(R.string.details_field_tags))
        EditableTagChips(
            tags = tags,
            onRemoveTag = onRemoveTag,
            onAddTag = onAddTag,
        )
    }
}
