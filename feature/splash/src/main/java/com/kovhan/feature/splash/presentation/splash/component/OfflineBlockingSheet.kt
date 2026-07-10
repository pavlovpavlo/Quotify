package com.kovhan.feature.splash.presentation.splash.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.kovhan.core.ui.component.bottomsheet.QuotifyBottomSheet
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonSize
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OfflineBlockingSheet(
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    QuotifyBottomSheet(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensions.size24),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.size12),
        ) {
            Text(
                text = stringResource(R.string.splash_offline_title),
                style = typography.h4,
                color = colors.textPrimary,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(R.string.splash_offline_message),
                style = typography.body,
                color = colors.textSecondary,
                textAlign = TextAlign.Center,
            )
            QuotifyButton(
                text = stringResource(R.string.splash_offline_retry),
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth(),
                variant = QuotifyButtonVariant.Filled,
                accent = QuotifyButtonAccent.Primary,
                size = QuotifyButtonSize.Medium,
            )
        }
    }
}
