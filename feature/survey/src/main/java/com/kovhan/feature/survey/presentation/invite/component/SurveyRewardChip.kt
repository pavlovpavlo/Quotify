package com.kovhan.feature.survey.presentation.invite.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

@Composable
internal fun SurveyRewardChip(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(dimensions.radiusFull))
            .background(colors.accentPremiumSoft)
            .padding(horizontal = dimensions.size14, vertical = dimensions.size8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.size8),
    ) {
        Image(
            modifier = Modifier.size(dimensions.size16),
            painter = painterResource(DsR.drawable.ic_collection_gift),
            contentDescription = null,
            colorFilter = ColorFilter.tint(colors.accentPremium),
        )
        Text(
            text = text,
            color = colors.accentPremium,
            style = TextStyle(
                fontFamily = InterFamily,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.W600,
                lineHeight = 17.sp,
            ),
        )
    }
}
