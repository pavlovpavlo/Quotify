package com.kovhan.feature.addquote.presentation.addquote.component.voice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun VoiceEmptyState(modifier: Modifier = Modifier) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.space2),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.size14),
    ) {
        Text(
            text = stringResource(R.string.add_quote_voice_title),
            style = typography.readingBody.copy(fontWeight = FontWeight.W600),
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.widthIn(max = dimensions.size163 + dimensions.size96),
            text = stringResource(R.string.add_quote_voice_subtitle),
            style = typography.caption,
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}
