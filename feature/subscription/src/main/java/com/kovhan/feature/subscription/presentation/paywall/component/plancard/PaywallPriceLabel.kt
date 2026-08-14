package com.kovhan.feature.subscription.presentation.paywall.component.plancard

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
internal fun PaywallPriceLabel(
    amount: String,
    periodRes: Int?,
    amountStyle: TextStyle,
    periodStyle: TextStyle,
    amountColor: Color,
    periodColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.Bottom) {
        Text(text = amount, color = amountColor, style = amountStyle)
        if (periodRes != null) {
            Text(
                modifier = Modifier.padding(start = 1.dp, bottom = 2.dp),
                text = stringResource(periodRes),
                color = periodColor,
                style = periodStyle,
            )
        }
    }
}
