package com.kovhan.feature.widget.presentation.appearance.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kovhan.core.ui.component.text.QuotifyFieldLabel
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
internal fun WidgetLabeledSection(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val dimensions = QuotifyMaterialTheme.dimensions

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensions.space3),
    ) {
        QuotifyFieldLabel(text = label)
        content()
    }
}
