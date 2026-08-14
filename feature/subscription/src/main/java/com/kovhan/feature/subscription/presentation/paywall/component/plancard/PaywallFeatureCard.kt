package com.kovhan.feature.subscription.presentation.paywall.component.plancard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.subscription.presentation.common.premiumFeatures
import com.kovhan.feature.subscription.presentation.common.rememberBoldMarkup

@Composable
internal fun PaywallFeatureCard(modifier: Modifier = Modifier) {
    val premium = QuotifyMaterialTheme.colors.premium
    val dimensions = QuotifyMaterialTheme.dimensions

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensions.radiusXl))
            .background(premium.card)
            .padding(horizontal = 18.dp),
    ) {
        premiumFeatures.forEachIndexed { index, feature ->
            if (index > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(premium.cardDivider),
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 13.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(13.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(premium.iconBackground, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(feature.iconRes),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(premium.iconTint),
                    )
                }

                Text(
                    text = rememberBoldMarkup(stringResource(feature.textRes)),
                    color = premium.cardText,
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontSize = 13.5.sp,
                        lineHeight = 19.sp,
                    ),
                )
            }
        }
    }
}
