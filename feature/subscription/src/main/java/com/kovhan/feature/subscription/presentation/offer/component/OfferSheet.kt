package com.kovhan.feature.subscription.presentation.offer.component

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.ui.component.button.QuotifyTextBtn
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.subscription.presentation.common.PremiumGradientButton
import com.kovhan.feature.subscription.presentation.paywall.component.PaywallLegalText
import com.kovhan.design.systems.R as DsR

@Composable
internal fun OfferSheet(
    priceNow: String,
    priceWas: String?,
    discountPercent: Int?,
    perMonth: String?,
    isPurchasing: Boolean,
    onClaim: () -> Unit,
    onDismiss: () -> Unit,
    onOpenLink: (title: String, url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(colors.bgElevated)
            .padding(horizontal = 24.dp, vertical = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(DsR.string.offer_title),
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontFamily = NewsreaderFamily,
                fontSize = 24.sp,
                fontWeight = FontWeight.W600,
                lineHeight = 28.sp,
            ),
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(DsR.string.offer_subtitle),
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontFamily = InterFamily,
                fontSize = 14.5.sp,
                lineHeight = 21.sp,
            ),
        )

        PriceRow(
            priceNow = priceNow,
            priceWas = priceWas,
            discountPercent = discountPercent,
            modifier = Modifier.padding(top = 18.dp),
        )

        if (perMonth != null) {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(DsR.string.offer_per_month, perMonth),
                color = colors.accentPremiumHover,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.W600,
                ),
            )
        }

        PremiumGradientButton(
            modifier = Modifier.padding(top = 20.dp),
            text = stringResource(DsR.string.offer_cta, priceNow),
            onClick = onClaim,
            loading = isPurchasing,
            height = 54.dp,
            brush = colors.offer.ctaGradient,
        )

        QuotifyTextBtn(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(DsR.string.offer_dismiss),
            onClick = onDismiss,
        )

        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = stringResource(DsR.string.offer_note),
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
            style = TextStyle(fontFamily = InterFamily, fontSize = 12.sp),
        )

        PaywallLegalText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            onOpenLink = onOpenLink,
        )
    }
}

@Composable
private fun PriceRow(
    priceNow: String,
    priceWas: String?,
    discountPercent: Int?,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = priceNow,
                color = colors.textPrimary,
                style = TextStyle(
                    fontFamily = NewsreaderFamily,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.W700,
                ),
            )
            Text(
                modifier = Modifier.padding(start = 2.dp, bottom = 3.dp),
                text = stringResource(DsR.string.paywall_period_year),
                color = colors.textSecondary,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W600,
                ),
            )
        }

        if (priceWas != null) {
            Text(
                modifier = Modifier.padding(bottom = 6.dp),
                text = priceWas,
                color = colors.textTertiary,
                textDecoration = TextDecoration.LineThrough,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                ),
            )
        }

        if (discountPercent != null) {
            Text(
                modifier = Modifier
                    .padding(bottom = 6.dp)
                    .clip(RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusFull))
                    .background(colors.accentSavedSoft)
                    .padding(horizontal = 9.dp, vertical = 4.dp),
                text = stringResource(DsR.string.offer_discount, discountPercent),
                color = colors.accentSaved,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.W700,
                ),
            )
        }
    }
}
