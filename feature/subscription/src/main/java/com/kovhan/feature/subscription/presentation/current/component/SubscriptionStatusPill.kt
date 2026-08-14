package com.kovhan.feature.subscription.presentation.current.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.models.billing.SubscriptionStatus
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun SubscriptionStatusPill(
    status: String?,
    isEntitled: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors

    val labelRes = when (status) {
        SubscriptionStatus.ACTIVE -> R.string.subscription_status_active
        SubscriptionStatus.CANCELED -> R.string.subscription_status_canceled
        SubscriptionStatus.IN_GRACE_PERIOD -> R.string.subscription_status_grace
        SubscriptionStatus.ON_HOLD -> R.string.subscription_status_on_hold
        SubscriptionStatus.PAUSED -> R.string.subscription_status_paused
        SubscriptionStatus.EXPIRED -> R.string.subscription_status_expired
        else -> if (isEntitled) R.string.subscription_status_active else R.string.subscription_status_expired
    }

    // Скасована підписка ще діє до кінця періоду — не фарбуємо її в помилку.
    val (content: Color, container: Color) = when {
        isEntitled -> colors.accentSaved to colors.accentSavedSoft
        status == SubscriptionStatus.ON_HOLD -> colors.error to colors.errorSoft
        else -> colors.textSecondary to colors.bgSecondary
    }

    Text(
        modifier = modifier
            .clip(CircleShape)
            .background(container)
            .padding(horizontal = 11.dp, vertical = 4.dp),
        text = stringResource(labelRes),
        color = content,
        style = TextStyle(
            fontFamily = InterFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.W600,
        ),
    )
}
