package com.kovhan.feature.subscription.presentation.paywall.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.core.models.billing.PremiumOffer
import com.kovhan.core.ui.extensions.debouncedClickable
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.subscription.presentation.common.displayPrice
import com.kovhan.feature.subscription.presentation.common.planCopy
import com.kovhan.design.systems.R as DsR

/** Один рядок вибору плану: радіо, назва з ціною і позначка «Популярний» або період оплати. */
@Composable
internal fun PaywallPlanRow(
    offer: PremiumOffer,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val copy = planCopy(offer.basePlanId)
    val locale = LocalConfiguration.current.locales[0]
    val shape = RoundedCornerShape(dimensions.radiusLg)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(if (selected) colors.accentSavedSoft else colors.bgElevated)
            .border(
                width = 1.5.dp,
                color = if (selected) colors.accentSaved else colors.border,
                shape = shape,
            )
            .debouncedClickable(debounceInterval = 200L, onClick = onClick)
            .padding(horizontal = dimensions.space4, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.space3),
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(if (selected) colors.accentSaved else Color.Transparent)
                .border(
                    width = 2.dp,
                    color = if (selected) colors.accentSaved else colors.borderStrong,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier
                    .size(13.dp)
                    .alpha(if (selected) 1f else 0f),
                painter = painterResource(DsR.drawable.ic_check),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colors.textOnAccent),
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensions.space2),
        ) {
            if (copy.shortNameRes != null) {
                Text(
                    text = stringResource(copy.shortNameRes),
                    color = colors.textPrimary,
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.W500,
                    ),
                )
            }
            Text(
                text = offer.displayPrice(locale),
                color = colors.textPrimary,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W700,
                ),
            )
        }

        if (copy.isPopular) {
            Text(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colors.accentSavedSoft)
                    .padding(horizontal = 11.dp, vertical = 4.dp),
                text = stringResource(DsR.string.paywall_plan_tag_popular),
                color = colors.accentSavedHover,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.W600,
                ),
            )
        } else if (copy.metaRes != null) {
            Text(
                text = stringResource(copy.metaRes),
                color = colors.textSecondary,
                style = TextStyle(fontFamily = InterFamily, fontSize = 13.sp),
            )
        }
    }
}
