package com.kovhan.feature.main.presentation.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.QuotifyTopBar
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.main.presentation.subscription.mvi.CurrentSubscriptionState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CurrentSubscriptionScreen(
    state: CurrentSubscriptionState,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onOpenPaywall: () -> Unit,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors
    val status = state.status

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgPrimary)
            .padding(top = paddingValues.calculateTopPadding()),
    ) {
        QuotifyTopBar(title = "Поточна підписка (тест)", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = if (state.isLoading) {
                    "Завантаження…"
                } else {
                    "план: ${if (state.isEntitled) "Premium" else "Free"}\n" +
                        "isActive: ${status.isActive}\n" +
                        "status: ${status.status ?: "—"}\n" +
                        "productId: ${status.productId ?: "—"}\n" +
                        "expiresAt: ${status.expiresAt.formatDate()}\n" +
                        "autoRenewing: ${status.autoRenewing}"
                },
                color = colors.textPrimary,
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onRefresh,
            ) {
                Text("Оновити статус")
            }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onOpenPaywall,
            ) {
                Text("Екран покупки")
            }
        }
    }
}

private fun Long?.formatDate(): String {
    if (this == null || this <= 0L) return "—"
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return "${formatter.format(Date(this))} ($this)"
}
