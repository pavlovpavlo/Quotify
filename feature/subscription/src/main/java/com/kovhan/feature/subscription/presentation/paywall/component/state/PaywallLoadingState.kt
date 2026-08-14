package com.kovhan.feature.subscription.presentation.paywall.component.state

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
internal fun PaywallLoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = QuotifyMaterialTheme.colors.accentPrimary)
    }
}
