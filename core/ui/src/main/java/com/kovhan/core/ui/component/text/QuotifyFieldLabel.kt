package com.kovhan.core.ui.component.text

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kovhan.design.systems.QuotifyMaterialTheme

/** Small eyebrow label placed above a form field (quote / author / book / tags / …). */
@Composable
fun QuotifyFieldLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    Text(
        modifier = modifier.padding(bottom = dimensions.space2, start = dimensions.size2),
        text = text,
        style = typography.eyebrow,
        color = colors.textTertiary,
    )
}
