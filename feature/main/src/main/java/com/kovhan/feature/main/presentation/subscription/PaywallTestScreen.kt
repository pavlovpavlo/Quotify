package com.kovhan.feature.main.presentation.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kovhan.core.models.billing.PremiumOffer
import com.kovhan.core.ui.component.QuotifyTopBar
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.main.presentation.subscription.mvi.PaywallState

@Composable
fun PaywallTestScreen(
    state: PaywallState,
    onBack: () -> Unit,
    onRestore: () -> Unit,
    onReload: () -> Unit,
    onBuy: (PremiumOffer) -> Unit,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgPrimary)
            .padding(top = paddingValues.calculateTopPadding()),
    ) {
        QuotifyTopBar(title = "Підписки (тест)", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = paddingValues.calculateBottomPadding() + 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onRestore,
            ) {
                Text("Відновити покупки")
            }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onReload,
            ) {
                Text("Перезапитати товар")
            }

            Text(
                text = if (state.isPremium) {
                    "Підписка АКТИВНА (${state.subscriptionStatus ?: "—"})"
                } else {
                    "Підписки немає (${state.subscriptionStatus ?: "—"})"
                },
                color = colors.textPrimary,
            )

            Text(
                text = when {
                    state.isLoading -> "Завантаження…"
                    state.productId == null -> "Товар недоступний"
                    else -> "productId: ${state.productId}"
                },
                color = colors.textPrimary,
            )

            state.offers.forEach { offer ->
                Text(
                    text = "basePlanId: ${offer.basePlanId}\n" +
                        "offerId: ${offer.offerId ?: "—"}\n" +
                        "ціна: ${offer.formattedPrice}\n" +
                        "offerToken: ${offer.offerToken.take(24)}…",
                    color = colors.textSecondary,
                )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onBuy(offer) },
                ) {
                    Text("Купити ${offer.basePlanId}")
                }
            }

            if (state.log.isNotEmpty()) {
                Text(text = "— лог —", color = colors.textTertiary)
                state.log.asReversed().forEach { line ->
                    Text(text = line, color = colors.textSecondary)
                }
            }
        }
    }
}
