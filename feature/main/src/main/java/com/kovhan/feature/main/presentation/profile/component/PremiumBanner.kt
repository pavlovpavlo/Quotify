package com.kovhan.feature.main.presentation.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun PremiumBanner(
    onUpgradeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val shape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusXl)
    val isDark = colors.bgPrimary.luminance() < 0.5f
    val chipBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.White.copy(alpha = 0.45f)

    Column(
        modifier = modifier
            .shadow(2.dp, shape)
            .clip(shape)
            .background(colors.accentPremiumSoft)
            .border(1.dp, colors.accentPremium.copy(alpha = 0.4f), shape)
            .padding(18.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(11.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier
                    .shadow(6.dp, CircleShape)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colors.accentPremium),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.size(21.dp),
                    painter = painterResource(R.drawable.ic_crown),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                )
            }

            Column {
                Text(
                    text = stringResource(R.string.profile_premium_eyebrow),
                    color = colors.accentPremiumHover,
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.W700,
                        letterSpacing = 0.14.em,
                    ),
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = stringResource(R.string.profile_premium_title),
                    color = colors.textPrimary,
                    style = TextStyle(
                        fontFamily = NewsreaderFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W600,
                    ),
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            BenefitChip(stringResource(R.string.profile_premium_benefit_folders), chipBg)
            BenefitChip(stringResource(R.string.profile_premium_benefit_widgets), chipBg)
            BenefitChip(stringResource(R.string.profile_premium_benefit_sync), chipBg)
        }

        Spacer(Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .background(colors.accentPremiumHover)
                .clickable(onClick = onUpgradeClick)
                .padding(vertical = 11.dp, horizontal = 22.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.profile_premium_cta),
                color = colors.textOnAccent,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600,
                ),
            )
        }
    }
}

@Composable
private fun BenefitChip(
    text: String,
    background: Color,
) {
    val colors = QuotifyMaterialTheme.colors
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(background)
            .border(1.dp, colors.accentPremium.copy(alpha = 0.3f), CircleShape)
            .padding(start = 7.dp, end = 10.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Image(
            modifier = Modifier.size(13.dp),
            painter = painterResource(R.drawable.ic_check),
            contentDescription = null,
            colorFilter = ColorFilter.tint(colors.accentPremiumHover),
        )
        Text(
            text = text,
            color = colors.textSecondary,
            style = TextStyle(
                fontFamily = InterFamily,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.W500,
            ),
        )
    }
}
