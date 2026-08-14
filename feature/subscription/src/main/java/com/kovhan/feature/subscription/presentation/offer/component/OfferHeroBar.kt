package com.kovhan.feature.subscription.presentation.offer.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.component.button.QuotifyIconButton
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

@Composable
internal fun OfferHeroBar(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val offerColors = colors.offer

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .clip(CircleShape)
                .background(colors.accentPrimary)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            text = stringResource(DsR.string.offer_badge),
            color = Color.White,
            style = TextStyle(
                fontFamily = InterFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.W700,
                letterSpacing = 1.7.sp,
            ),
        )

        QuotifyIconButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = onClose,
            size = 36.dp,
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(offerColors.closeScrim),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(DsR.drawable.ic_close),
                    contentDescription = stringResource(DsR.string.offer_close_cd),
                    colorFilter = ColorFilter.tint(offerColors.closeIcon),
                )
            }
        }
    }
}
