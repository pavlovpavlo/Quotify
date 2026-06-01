package com.kovhan.core.ui.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
fun QuotifySnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
) {
    val palette = QuotifyMaterialTheme.colors
    val visuals = snackbarData.visuals
    val type = (visuals as? QuotifySnackbarVisuals)?.type ?: SnackbarType.Info

    val container: Color = when (type) {
        SnackbarType.Error -> palette.error
        SnackbarType.Success -> palette.accentSaved
        SnackbarType.Info -> palette.bgElevated
    }
    val content: Color = when (type) {
        SnackbarType.Info -> palette.textPrimary
        else -> palette.textOnAccent
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusLg))
            .background(container)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(
            text = visuals.message,
            color = content,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            style = QuotifyMaterialTheme.typography.body,
        )
    }
}
