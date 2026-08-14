package com.kovhan.feature.subscription.presentation.current.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.models.billing.SubscriptionStatus
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.subscription.presentation.common.formatMoney
import com.kovhan.feature.subscription.presentation.common.planCopy
import com.kovhan.feature.subscription.presentation.common.premiumFeatures
import com.kovhan.feature.subscription.presentation.common.rememberBoldMarkup
import java.text.DateFormat
import java.util.Date
import com.kovhan.design.systems.R as DsR

@Composable
internal fun SubscriptionPlanCard(
    status: SubscriptionStatus,
    isEntitled: Boolean,
    planPriceMicros: Long?,
    planCurrency: String?,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val shape = RoundedCornerShape(dimensions.radiusXl)
    val copy = planCopy(status.basePlanId.orEmpty())
    val locale = LocalConfiguration.current.locales[0]
    val planPrice = if (planPriceMicros != null && planCurrency != null) {
        formatMoney(planPriceMicros, planCurrency, locale)
    } else {
        null
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(2.dp, shape)
            .clip(shape)
            .background(colors.bgElevated)
            .border(1.dp, colors.border, shape)
            .padding(18.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensions.space3),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(colors.accentPremiumSoft, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.size(22.dp),
                    painter = painterResource(DsR.drawable.ic_crown),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colors.accentPremium),
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(DsR.string.subscription_eyebrow),
                    color = colors.accentPremium,
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.W700,
                        letterSpacing = 1.1.sp,
                    ),
                )
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    // Поки бекенд не записав base plan, показуємо узагальнену назву.
                    text = stringResource(copy.fullNameRes ?: DsR.string.subscription_plan_fallback),
                    color = colors.textPrimary,
                    style = TextStyle(
                        fontFamily = NewsreaderFamily,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.W600,
                    ),
                )
            }

            SubscriptionStatusPill(status = status.status, isEntitled = isEntitled)
        }

        if (planPrice != null) {
            Text(
                modifier = Modifier.padding(top = 14.dp),
                text = planPriceLabel(planPrice, copy.periodRes),
                color = colors.textSecondary,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W600,
                ),
            )
        }

        val expiresAt = status.expiresAt?.takeIf { it > 0L }
        val startedAt = status.startedAt?.takeIf { it > 0L }

        if (expiresAt != null || startedAt != null) {
            CardDivider(modifier = Modifier.padding(top = 14.dp, bottom = 12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                if (expiresAt != null) {
                    MetaRow(
                        // Без автопродовження це вже не «наступне списання», а дата завершення.
                        labelRes = if (status.autoRenewing) {
                            DsR.string.subscription_next_billing
                        } else {
                            DsR.string.subscription_valid_until
                        },
                        value = rememberFormattedDate(expiresAt),
                    )
                }
                if (startedAt != null) {
                    MetaRow(
                        labelRes = DsR.string.subscription_start_date,
                        value = rememberFormattedDate(startedAt),
                    )
                }
            }
        }

        CardDivider(modifier = Modifier.padding(top = 14.dp, bottom = 16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            premiumFeatures.forEach { feature ->
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.space3),
                ) {
                    Image(
                        modifier = Modifier
                            .padding(top = 1.dp)
                            .size(22.dp),
                        painter = painterResource(feature.iconRes),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(colors.accentPrimary),
                    )
                    Text(
                        text = rememberBoldMarkup(
                            text = stringResource(feature.textRes),
                            boldWeight = FontWeight.W600,
                            boldColor = colors.textPrimary,
                        ),
                        color = colors.textSecondary,
                        style = TextStyle(
                            fontFamily = InterFamily,
                            fontSize = 14.5.sp,
                            lineHeight = 20.sp,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun MetaRow(labelRes: Int, value: String) {
    val colors = QuotifyMaterialTheme.colors

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(labelRes),
            color = colors.textSecondary,
            style = TextStyle(fontFamily = InterFamily, fontSize = 13.5.sp),
        )
        Text(
            text = value,
            color = colors.textPrimary,
            style = TextStyle(
                fontFamily = InterFamily,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.W600,
            ),
        )
    }
}

/** «₴790/рік» — ціна від Play плюс позначка періоду з нашого словника. */
@Composable
private fun planPriceLabel(price: String, periodRes: Int?): String =
    if (periodRes == null) price else price + stringResource(periodRes)

@Composable
private fun CardDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(QuotifyMaterialTheme.colors.border),
    )
}

/** Локаль беремо з контексту, бо мова застосунку може відрізнятись від системної. */
@Composable
private fun rememberFormattedDate(timestamp: Long): String {
    val locale = LocalContext.current.resources.configuration.locales[0]
    return remember(timestamp, locale) {
        DateFormat.getDateInstance(DateFormat.LONG, locale).format(Date(timestamp))
    }
}
