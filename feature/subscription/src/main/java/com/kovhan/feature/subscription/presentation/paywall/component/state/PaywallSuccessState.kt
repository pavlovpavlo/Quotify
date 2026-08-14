package com.kovhan.feature.subscription.presentation.paywall.component.state

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.spacer.VerticalSpacer
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

@Composable
internal fun PaywallSuccessState(
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val premium = colors.premium
    val dimensions = QuotifyMaterialTheme.dimensions

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 34.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .background(premium.gradient, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(36.dp),
                painter = painterResource(DsR.drawable.ic_check),
                contentDescription = null,
                colorFilter = ColorFilter.tint(premium.onGradient),
            )
        }

        VerticalSpacer(18.dp)

        Text(
            text = stringResource(DsR.string.paywall_success_title),
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontFamily = NewsreaderFamily,
                fontSize = 26.sp,
                fontWeight = FontWeight.W600,
                letterSpacing = (-0.26).sp,
            ),
        )

        VerticalSpacer(dimensions.space2)

        Text(
            modifier = Modifier.widthIn(max = 280.dp),
            text = stringResource(DsR.string.paywall_success_message),
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
            style = QuotifyMaterialTheme.typography.body.copy(fontSize = 14.sp),
        )

        VerticalSpacer(dimensions.space6)

        QuotifyButton(
            text = stringResource(DsR.string.paywall_success_cta),
            onClick = onDone,
            sizeSpec = QuotifyButtonDefaults.pillSizeSpec(),
        )
    }
}
