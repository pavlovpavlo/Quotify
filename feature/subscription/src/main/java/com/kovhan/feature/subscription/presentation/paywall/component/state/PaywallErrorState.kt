package com.kovhan.feature.subscription.presentation.paywall.component.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonSize
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.core.ui.component.spacer.VerticalSpacer
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

@Composable
internal fun PaywallErrorState(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 34.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(DsR.string.paywall_load_error_title),
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
            style = QuotifyMaterialTheme.typography.h4,
        )
        VerticalSpacer(dimensions.space2)
        Text(
            text = stringResource(DsR.string.paywall_load_error_message),
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
            style = QuotifyMaterialTheme.typography.body.copy(fontSize = 14.sp),
        )
        VerticalSpacer(dimensions.space6)
        QuotifyButton(
            text = stringResource(DsR.string.paywall_retry),
            onClick = onRetry,
            variant = QuotifyButtonVariant.Outlined,
            accent = QuotifyButtonAccent.Neutral,
            size = QuotifyButtonSize.Medium,
        )
    }
}
