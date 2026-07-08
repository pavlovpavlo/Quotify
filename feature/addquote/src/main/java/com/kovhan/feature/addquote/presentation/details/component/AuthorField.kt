package com.kovhan.feature.addquote.presentation.details.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kovhan.core.ui.component.combobox.QuotifyCombobox
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun AuthorField(
    query: String,
    onQueryChange: (String) -> Unit,
    onPicked: (String) -> Unit,
    options: List<String>,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(bottom = dimensions.space2, start = dimensions.size2),
            text = stringResource(R.string.details_field_author),
            style = typography.eyebrow,
            color = colors.textTertiary,
        )
        QuotifyCombobox(
            value = query,
            onValueChange = onQueryChange,
            options = options,
            onOptionSelected = onPicked,
            placeholder = stringResource(R.string.details_author_ph),
            emptyText = stringResource(R.string.details_combo_empty),
        )
    }
}
