package com.kovhan.feature.subscription.presentation.paywall.component.plancard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.models.billing.PremiumOffer
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.subscription.presentation.common.displayPrice
import com.kovhan.feature.subscription.presentation.common.monthlyEquivalentOrNull
import com.kovhan.feature.subscription.presentation.common.planCopy
import java.util.Locale
import com.kovhan.design.systems.R as DsR


@Composable
internal fun PaywallPlanCard(
    offer: PremiumOffer,
    locale: Locale,
    modifier: Modifier = Modifier,
) {
    val premium = QuotifyMaterialTheme.colors.premium
    val dimensions = QuotifyMaterialTheme.dimensions
    val copy = planCopy(offer.basePlanId)

    val fullPrice = offer.displayPrice(locale)
    val monthlyEquivalent = offer.monthlyEquivalentOrNull(locale)
    val mainPrice = monthlyEquivalent ?: fullPrice
    val mainPeriodRes = if (monthlyEquivalent != null) {
        DsR.string.paywall_period_month
    } else {
        copy.periodRes
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensions.radius2xl))
            .background(premium.gradient)
            .padding(horizontal = 22.dp, vertical = 20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            PaywallPriceLabel(
                amount = mainPrice,
                periodRes = mainPeriodRes,
                amountStyle = TextStyle(
                    fontFamily = NewsreaderFamily,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.W600,
                    letterSpacing = (-0.7).sp,
                ),
                periodStyle = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                ),
                amountColor = premium.onGradient,
                periodColor = premium.onGradientMuted,
            )

            if (monthlyEquivalent != null) {
                PaywallPriceLabel(
                    amount = fullPrice,
                    periodRes = copy.periodRes,
                    amountStyle = TextStyle(
                        fontFamily = NewsreaderFamily,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.W500,
                    ),
                    periodStyle = TextStyle(
                        fontFamily = InterFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W500,
                    ),
                    amountColor = premium.onGradientMuted,
                    periodColor = premium.onGradientSubtle,
                )
            }
        }

        if (copy.fullNameRes != null) {
            Text(
                modifier = Modifier.padding(top = dimensions.space4),
                text = stringResource(copy.fullNameRes),
                color = premium.onGradient,
                style = TextStyle(
                    fontFamily = NewsreaderFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.W600,
                    letterSpacing = (-0.22).sp,
                ),
            )
        }

        if (copy.descriptionRes != null) {
            Text(
                modifier = Modifier.padding(top = dimensions.space1 + 2.dp),
                text = stringResource(copy.descriptionRes),
                color = premium.onGradientMuted,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                ),
            )
        }

        Text(
            modifier = Modifier.padding(top = 18.dp, bottom = dimensions.space3),
            text = stringResource(DsR.string.paywall_plan_gets),
            color = premium.onGradient,
            style = TextStyle(
                fontFamily = InterFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
            ),
        )

        PaywallFeatureCard()
    }
}
