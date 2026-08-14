package com.kovhan.feature.subscription.presentation.current

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.component.QuotifyTopBar
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.core.ui.component.spacer.VerticalSpacer
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.subscription.presentation.current.component.SubscriptionPlanCard
import com.kovhan.feature.subscription.presentation.current.mvi.CurrentSubscriptionState
import com.kovhan.design.systems.R as DsR

@Composable
fun CurrentSubscriptionScreen(
    state: CurrentSubscriptionState,
    onBack: () -> Unit,
    onManage: () -> Unit,
    onOpenPaywall: () -> Unit,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgPrimary)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            ),
    ) {
        QuotifyTopBar(
            title = stringResource(DsR.string.subscription_current_title),
            onBack = onBack,
        )

        when {
            state.isLoading -> Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = colors.accentPrimary)
            }

            !state.isEntitled -> EmptyState(
                onOpenPaywall = onOpenPaywall,
                modifier = Modifier.weight(1f),
            )

            else -> {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 18.dp, vertical = 20.dp),
                ) {
                    SubscriptionPlanCard(
                        status = state.status,
                        isEntitled = state.isEntitled,
                        planPriceMicros = state.planPriceMicros,
                        planCurrency = state.planCurrency,
                    )
                }

                ManageFooter(onManage = onManage)
            }
        }
    }
}

@Composable
private fun ManageFooter(onManage: () -> Unit) {
    val colors = QuotifyMaterialTheme.colors

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colors.border),
        )
        QuotifyButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            text = stringResource(DsR.string.subscription_manage_cta),
            onClick = onManage,
            variant = QuotifyButtonVariant.Tonal,
            accent = QuotifyButtonAccent.Premium,
            sizeSpec = QuotifyButtonDefaults.pillSizeSpec(height = 52.dp),
            leadingIcon = painterResource(DsR.drawable.ic_info),
        )
    }
}

@Composable
private fun EmptyState(onOpenPaywall: () -> Unit, modifier: Modifier = Modifier) {
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
            text = stringResource(DsR.string.subscription_empty_title),
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
            style = QuotifyMaterialTheme.typography.h4,
        )
        VerticalSpacer(dimensions.space2)
        Text(
            text = stringResource(DsR.string.subscription_empty_message),
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
            style = QuotifyMaterialTheme.typography.body.copy(fontSize = 14.sp),
        )
        VerticalSpacer(dimensions.space6)
        QuotifyButton(
            text = stringResource(DsR.string.subscription_empty_cta),
            onClick = onOpenPaywall,
            sizeSpec = QuotifyButtonDefaults.pillSizeSpec(),
        )
    }
}
